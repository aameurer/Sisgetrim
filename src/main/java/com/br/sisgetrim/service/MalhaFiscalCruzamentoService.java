package com.br.sisgetrim.service;

import com.br.sisgetrim.dto.MalhaResultadoDTO;
import com.br.sisgetrim.model.*;
import com.br.sisgetrim.repository.MalhaLoteRepository;
import com.br.sisgetrim.repository.MalhaResultadoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de cruzamento da Malha Fiscal ITBI.
 * Executa JOIN entre DOI e FISCAL, aplica 4 regras e persiste resultados.
 */
@Service
public class MalhaFiscalCruzamentoService {

    private static final Logger log = LoggerFactory.getLogger(MalhaFiscalCruzamentoService.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final MalhaLoteRepository loteRepository;
    private final MalhaResultadoRepository resultadoRepository;

    public MalhaFiscalCruzamentoService(MalhaLoteRepository loteRepository,
            MalhaResultadoRepository resultadoRepository) {
        this.loteRepository = loteRepository;
        this.resultadoRepository = resultadoRepository;
    }

    private static final String SQL_CRUZAMENTO = """
            SELECT d.id                                              AS doi_id,
                   d.matricula                                       AS doi_matricula,
                   d.data_lavratura                                  AS doi_data_lavratura,
                   d.situacao                                        AS doi_situacao,
                   COALESCE(op.valor_base_calculo_itbi_itcmd, 0)     AS doi_valor_bc,
                   f.id                                              AS fiscal_id,
                   f.cadimo_inscricao                                AS fiscal_inscricao,
                   f.cadimo_cib                                      AS fiscal_cib,
                   f.cadimo_quadra                                   AS fiscal_quadra,
                   f.cadimo_lote                                     AS fiscal_lote,
                   f.cadimo_bairro_nome                              AS fiscal_bairro,
                   COALESCE(f.itbi_valor_venal_calculado_total, 0)   AS fiscal_valor_venal,
                   f.itbi_situacao                                   AS fiscal_situacao,
                   c.denominacao                                     AS cartorio_nome,
                   op.tipo_operacao_imobiliaria                      AS tipo_operacao
            FROM doi_declaracoes d
            JOIN doi_operacao_imobiliaria op ON op.doi_declaracao_id = d.id
            LEFT JOIN doi_imoveis di_imv ON di_imv.declaracao_id = d.id
            JOIN fiscal_itbi f ON f.entidade_id = d.entidade_id
                 AND (
                     (d.matricula IS NOT NULL AND f.cadimo_matricula IS NOT NULL AND f.cadimo_matricula = d.matricula)
                     OR
                     (di_imv.cib IS NOT NULL AND f.cadimo_cib IS NOT NULL AND f.cadimo_cib = di_imv.cib)
                 )
            LEFT JOIN doi_importacoes di ON di.id = d.importacao_id
            LEFT JOIN cartorios c ON c.id = di.cartorio_id
            WHERE d.entidade_id = :entidadeId
              AND d.data_lavratura BETWEEN :dataInicial AND :dataFinal
            ORDER BY d.data_lavratura DESC
            """;

    /**
     * Executa o cruzamento, persiste lote + resultados, e retorna DTOs.
     */
    @Transactional
    @SuppressWarnings("unchecked")
    public MalhaLote executarCruzamento(Entidade entidade, Usuario usuario, ParametroMalha parametros) {
        log.info("Iniciando cruzamento da malha para entidade ID={}", entidade.getId());

        LocalDate dataInicial = parametros.getDataInicial();
        LocalDate dataFinal = parametros.getDataFinal();

        if (dataInicial == null || dataFinal == null) {
            int ano = LocalDate.now().getYear();
            dataInicial = LocalDate.of(ano, 1, 1);
            dataFinal = LocalDate.of(ano, 12, 31);
        }

        // 1. Criar lote
        MalhaLote lote = new MalhaLote();
        lote.setEntidade(entidade);
        lote.setUsuario(usuario);
        lote.setDataInicial(dataInicial);
        lote.setDataFinal(dataFinal);
        lote.setDiferencaBcDoi(parametros.getDiferencaBcDoi());
        lote.setDiferencaImpostoDoi(parametros.getDiferencaImpostoDoi());
        lote.setPercentualAbaixoVvi(parametros.getPercentualAbaixoVvi());
        lote.setPercentualAbaixoImpostoDoi(parametros.getPercentualAbaixoImpostoDoi());
        lote.setConsiderarIntegralizacaoCapital(parametros.getConsiderarIntegralizacaoCapital());
        lote.setStatus("PROCESSANDO");
        lote = loteRepository.save(lote);

        // 2. Executar query
        Query query = entityManager.createNativeQuery(SQL_CRUZAMENTO);
        query.setParameter("entidadeId", entidade.getId());
        query.setParameter("dataInicial", Date.valueOf(dataInicial));
        query.setParameter("dataFinal", Date.valueOf(dataFinal));

        List<Object[]> rows = query.getResultList();
        log.info("Cruzamento retornou {} registros brutos", rows.size());

        // 3. Aplicar regras e persistir resultados
        BigDecimal thresholdBC = defaultBD(parametros.getDiferencaBcDoi(), BigDecimal.ONE);
        BigDecimal thresholdImposto = defaultBD(parametros.getDiferencaImpostoDoi(), BigDecimal.ONE);
        Double thresholdPercentVvi = parametros.getPercentualAbaixoVvi() != null
                ? parametros.getPercentualAbaixoVvi()
                : 1.0;
        Boolean considerarIntegralizacao = parametros.getConsiderarIntegralizacaoCapital();

        List<MalhaResultado> resultados = new ArrayList<>();

        for (Object[] row : rows) {
            BigDecimal valorDoi = toBigDecimal(row[4]);
            BigDecimal valorFiscal = toBigDecimal(row[11]);
            String tipoOperacao = toStr(row[14]);

            List<String> violacoes = new ArrayList<>();
            BigDecimal diferenca = valorDoi.subtract(valorFiscal).abs();

            // Regra 1: Diferença BC > threshold
            if (diferenca.compareTo(thresholdBC) > 0) {
                violacoes.add("Diferença BC > R$ " + formatMoeda(thresholdBC));
            }

            // Regra 2: Alerta Imposto
            if (diferenca.compareTo(thresholdImposto) > 0 && diferenca.compareTo(thresholdBC) <= 0) {
                violacoes.add("Alerta: Diferença Imposto > R$ " + formatMoeda(thresholdImposto));
            }

            // Regra 3: DOI abaixo % VVI
            if (valorFiscal.compareTo(BigDecimal.ZERO) > 0 && thresholdPercentVvi > 0) {
                BigDecimal fator = BigDecimal.ONE.subtract(
                        BigDecimal.valueOf(thresholdPercentVvi)
                                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP));
                BigDecimal limiteMinimo = valorFiscal.multiply(fator);
                if (valorDoi.compareTo(limiteMinimo) < 0) {
                    violacoes.add("DOI " + thresholdPercentVvi.intValue() + "% abaixo do VVI");
                }
            }

            // Regra 4: Integralização de Capital
            if (Boolean.TRUE.equals(considerarIntegralizacao) && tipoOperacao != null) {
                String tipoNorm = tipoOperacao.toUpperCase().trim();
                if (tipoNorm.contains("INTEGRALIZACAO") || tipoNorm.contains("INTEGRALIZAÇÃO")
                        || tipoNorm.contains("CAPITAL")) {
                    violacoes.add("Integralização de Capital");
                }
            }

            if (!violacoes.isEmpty()) {
                MalhaResultado res = new MalhaResultado();
                res.setLote(lote);
                res.setEntidade(entidade);
                res.setDoiId(toLong(row[0]));
                res.setMatricula(toStr(row[1]));
                res.setDataLavratura(toLocalDate(row[2]));
                res.setSituacaoDoi(toStr(row[3]));
                res.setValorBaseCalculoDoi(valorDoi);
                res.setFiscalId(toLong(row[5]));
                res.setCadimoInscricao(toStr(row[6]));
                res.setCib(toStr(row[7]));
                res.setCadimoQuadra(toStr(row[8]));
                res.setCadimoLote(toStr(row[9]));
                res.setCadimoBairroNome(toStr(row[10]));
                res.setValorVenalFiscal(valorFiscal);
                res.setDiferencaValor(diferenca);
                res.setCartorio(toStr(row[13]));
                res.setParametrosViolados(String.join(" | ", violacoes));
                res.setSituacao("PENDENTE");
                resultados.add(res);
            }
        }

        // 4. Salvar resultados em batch
        resultadoRepository.saveAll(resultados);

        // 5. Atualizar lote com métricas
        lote.setTotalAnalisado(rows.size());
        lote.setTotalDivergencias(resultados.size());
        lote.setStatus("CONCLUIDO");
        loteRepository.save(lote);

        log.info("Malha gerada: lote ID={}, {} divergências de {} cruzados",
                lote.getId(), resultados.size(), rows.size());

        return lote;
    }

    /**
     * Converte resultados persistidos em DTOs para o frontend.
     */
    public List<MalhaResultadoDTO> convertToDTO(List<MalhaResultado> resultados) {
        return resultados.stream().map(res -> {
            MalhaResultadoDTO dto = new MalhaResultadoDTO();
            dto.setDoiId(res.getDoiId());
            dto.setFiscalId(res.getFiscalId());
            dto.setMatricula(res.getMatricula());
            dto.setCadimoInscricao(res.getCadimoInscricao());
            dto.setCadimoCib(res.getCib());
            dto.setCadimoQuadra(res.getCadimoQuadra());
            dto.setCadimoLote(res.getCadimoLote());
            dto.setCadimoBairroNome(res.getCadimoBairroNome());
            dto.setDataLavratura(res.getDataLavratura());
            dto.setSituacao(res.getSituacao());
            dto.setValorBaseCalculoDoi(res.getValorBaseCalculoDoi());
            dto.setValorVenalFiscal(res.getValorVenalFiscal());
            dto.setDiferencaValor(res.getDiferencaValor());
            dto.setCartorio(res.getCartorio());
            if (res.getParametrosViolados() != null) {
                for (String p : res.getParametrosViolados().split("\\|")) {
                    dto.addParametroViolado(p.trim());
                }
            }
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Busca os últimos lotes de uma entidade.
     */
    public List<MalhaLote> buscarUltimosLotes(Entidade entidade) {
        return loteRepository.findTop10ByEntidadeOrderByCreatedAtDesc(entidade);
    }

    /**
     * Busca resultados de um lote específico.
     */
    public List<MalhaResultado> buscarResultadosPorLote(Long loteId) {
        return resultadoRepository.findByLoteIdOrderByDiferencaValorDesc(loteId);
    }

    // --- Helpers ---

    private BigDecimal defaultBD(BigDecimal val, BigDecimal fallback) {
        return val != null ? val : fallback;
    }

    private BigDecimal toBigDecimal(Object val) {
        if (val == null)
            return BigDecimal.ZERO;
        if (val instanceof BigDecimal)
            return (BigDecimal) val;
        if (val instanceof Number)
            return BigDecimal.valueOf(((Number) val).doubleValue());
        try {
            return new BigDecimal(val.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private Long toLong(Object val) {
        if (val == null)
            return null;
        if (val instanceof Number)
            return ((Number) val).longValue();
        try {
            return Long.valueOf(val.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private String toStr(Object val) {
        return val != null ? val.toString() : null;
    }

    private LocalDate toLocalDate(Object val) {
        if (val == null)
            return null;
        if (val instanceof java.sql.Date)
            return ((java.sql.Date) val).toLocalDate();
        if (val instanceof LocalDate)
            return (LocalDate) val;
        return null;
    }

    private String formatMoeda(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toString().replace('.', ',');
    }
}
