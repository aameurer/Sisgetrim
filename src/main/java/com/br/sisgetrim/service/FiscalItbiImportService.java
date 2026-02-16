package com.br.sisgetrim.service;

import com.br.sisgetrim.model.FiscalItbi;
import com.br.sisgetrim.model.FiscalItbiImportacao;
import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.repository.FiscalItbiRepository;
import com.br.sisgetrim.repository.EntidadeRepository;
import com.br.sisgetrim.repository.FiscalItbiImportacaoRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class FiscalItbiImportService {

    private final FiscalItbiRepository fiscalItbiRepository;
    private final EntidadeRepository entidadeRepository;
    private final ImportProgressService progressService;
    private final FiscalItbiImportacaoRepository itbiImportacaoRepository;
    private final UsuarioService usuarioService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public FiscalItbiImportService(FiscalItbiRepository fiscalItbiRepository,
            EntidadeRepository entidadeRepository,
            ImportProgressService progressService,
            FiscalItbiImportacaoRepository itbiImportacaoRepository,
            UsuarioService usuarioService) {
        this.fiscalItbiRepository = fiscalItbiRepository;
        this.entidadeRepository = entidadeRepository;
        this.progressService = progressService;
        this.itbiImportacaoRepository = itbiImportacaoRepository;
        this.usuarioService = usuarioService;
    }

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FiscalItbiImportService.class);

    @Transactional
    public int importarExcel(MultipartFile file, Long entidadeId) throws IOException {
        if (entidadeId == null)
            throw new IllegalArgumentException("entidadeId cannot be null");
        Entidade entidade = entidadeRepository.findById(entidadeId)
                .orElseThrow(() -> new RuntimeException("Entidade não encontrada com ID: " + entidadeId));

        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        Usuario usuarioAuth = (Usuario) auth.getPrincipal();
        Usuario usuario = usuarioService.buscarPorDocumento(usuarioAuth.getDocumento());

        FiscalItbiImportacao importacao = new FiscalItbiImportacao();
        importacao.setEntidade(entidade);
        importacao.setUsuario(usuario);
        importacao.setNomeArquivo(file.getOriginalFilename());
        importacao.setStatus("PROCESSANDO");
        importacao = itbiImportacaoRepository.save(importacao);

        progressService.setProgress(entidadeId, 0);

        // Diagnóstico de Importação
        Map<String, Integer> headerMap = new HashMap<>();
        List<String> firstLineData = new ArrayList<>();
        int totalImportado = 0;
        logger.info("Iniciando importação Excel para entidade: {} - {}", entidade.getId(),
                entidade.getNomeEmpresarial());

        try (InputStream is = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = findBestSheet(workbook);
            logger.info("Processando aba: '{}'", sheet.getSheetName());

            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) {
                logger.warn("O arquivo Excel está vazio.");
                return 0;
            }

            int rowCount = 0;
            List<String> lastCheckedHeaders = new ArrayList<>();

            while (rows.hasNext() && rowCount < 30) {
                Row currentRow = rows.next();
                rowCount++;

                if (isRowEmpty(currentRow))
                    continue;

                List<String> currentHeaders = new ArrayList<>();
                boolean looksLikeHeader = false;

                for (Cell cell : currentRow) {
                    try {
                        String val = getCellValueAsStringSafe(cell);
                        if (val != null && !val.trim().isEmpty()) {
                            String normalized = val.toLowerCase().trim();
                            currentHeaders.add(normalized);
                            if (normalized.contains("itbi") || normalized.contains("guia")
                                    || normalized.contains("numero")
                                    || normalized.contains("número") || normalized.contains("contribuinte")
                                    || normalized.contains("adquirente") || normalized.contains("transmitente")
                                    || normalized.contains("valor")) {
                                looksLikeHeader = true;
                            }
                        }
                    } catch (Exception e) {
                        /* ignore */ }
                }

                if (looksLikeHeader) {
                    headerMap.clear();
                    for (int i = 0; i < currentRow.getLastCellNum(); i++) {
                        Cell cell = currentRow.getCell(i);
                        String headerName = getCellValueAsStringSafe(cell);
                        if (headerName != null) {
                            headerMap.put(headerName.toLowerCase().trim(), i);
                        }
                    }
                    // Verifica se encontrou pelo menos a coluna básica de número
                    if (findColumnIndex(headerMap, "itbi - número", "itbi - numero", "numero_itbi", "itbi", "nº itbi",
                            "numero", "número") != null) {
                        logger.info("Cabeçalho identificado na linha {}", rowCount);
                        break;
                    } else {
                        lastCheckedHeaders = currentHeaders;
                        headerMap.clear();
                    }
                }
            }

            if (headerMap.isEmpty()) {
                String errorMsg = "Não foi possível identificar o cabeçalho da planilha. ";
                if (!lastCheckedHeaders.isEmpty()) {
                    errorMsg += "Colunas na última linha tentada: " + String.join(", ", lastCheckedHeaders);
                } else {
                    errorMsg += "Nenhuma linha com títulos reconhecidos encontrada nas primeiras 30 linhas.";
                }
                logger.error(errorMsg);
                throw new RuntimeException(errorMsg);
            }

            while (rows.hasNext()) {
                Row row = rows.next();
                if (isRowEmpty(row))
                    continue;

                // Captura exemplo da primeira linha de dados para diagnóstico se falhar
                if (firstLineData.isEmpty()) {
                    for (int i = 0; i < row.getLastCellNum(); i++) {
                        firstLineData.add(getCellValueAsStringSimple(row, i));
                    }
                }

                FiscalItbi fiscal = new FiscalItbi();
                fiscal.setEntidade(entidade);

                // --- SEÇÃO ITBI ---
                String numeroItbi = getCellValueAsString(row, headerMap, "itbi_numero", "itbi - número",
                        "itbi - numero", "nº itbi", "guia", "numero_itbi", "itbi", "nº", "nr_itbi", "nr itbi");
                Integer anoItbi = getCellValueAsInteger(row, headerMap, "itbi_ano", "itbi - ano", "exercicio",
                        "exercício", "ano_itbi", "ano", "exercício_itbi", "ano_guia");

                if (numeroItbi == null || (anoItbi == null || anoItbi == 0)) {
                    logger.debug("Pulando linha {}: Número={} / Ano={}", row.getRowNum() + 1, numeroItbi, anoItbi);
                    continue;
                }

                fiscal.setItbiNumero(numeroItbi);
                fiscal.setItbiAno(anoItbi);
                fiscal.setItbiData(getCellValueAsLocalDate(row, headerMap, "itbi_data", "itbi - data", "data",
                        "emissão", "emissao", "dt_itbi", "data itbi"));
                fiscal.setItbiTipo(getCellValueAsString(row, headerMap, "itbi_tipo", "itbi - tipo itbi", "tipo",
                        "descrição", "descricao"));
                fiscal.setItbiProprietarioNome(getCellValueAsString(row, headerMap, "itbi_proprietario_nome",
                        "itbi - proprietário principal - nome", "proprietarios_nomes", "proprietario"));
                fiscal.setItbiProprietarioCpf(
                        cleanDocumento(getCellValueAsString(row, headerMap, "itbi_proprietario_cpf",
                                "itbi - proprietário principal - cpf", "proprietarios_cpfs", "cpf proprietario")));
                fiscal.setItbiTransmitenteNome(getCellValueAsString(row, headerMap, "itbi_transmitente_nome",
                        "itbi - transmitente principal - nome", "transmitentes_nomes", "transmitente"));
                fiscal.setItbiTransmitenteCpf(
                        cleanDocumento(getCellValueAsString(row, headerMap, "itbi_transmitente_cpf",
                                "itbi - transmitente principal - cpf", "transmitentes_cpfs", "cpf transmitente")));
                fiscal.setItbiSituacao(getCellValueAsString(row, headerMap, "itbi_situacao", "itbi - situação itbi",
                        "itbi - situacao itbi", "situacao_itbi", "status"));
                fiscal.setItbiValorVenalCalculadoVvt(
                        getCellValueAsBigDecimal(row, headerMap, "itbi_valor_venal_calculado_vvt",
                                "itbi - valor venal calculado - v.v.t", "valor venal - territorial", "vvt_calc"));
                fiscal.setItbiValorVenalCalculadoVvp(
                        getCellValueAsBigDecimal(row, headerMap, "itbi_valor_venal_calculado_vvp",
                                "itbi - valor venal calculado - v.v.p", "valor venal - predial", "vvp_calc"));
                fiscal.setItbiValorVenalCalculadoTotal(
                        getCellValueAsBigDecimal(row, headerMap, "itbi_valor_venal_calculado_total",
                                "itbi - valor venal calculado - total", "valor venal - total", "vvt_total_calc"));
                fiscal.setItbiValorVenalInformadoVvt(getCellValueAsBigDecimal(row, headerMap,
                        "itbi_valor_venal_informado_vvt", "itbi - valor venal informado - v.v.t", "vvt_inf"));
                fiscal.setItbiValorVenalInformadoVvp(getCellValueAsBigDecimal(row, headerMap,
                        "itbi_valor_venal_informado_vvp", "itbi - valor venal informado - v.v.p", "vvp_inf"));
                fiscal.setItbiValorVenalInformadoTotal(getCellValueAsBigDecimal(row, headerMap,
                        "itbi_valor_venal_informado_total", "itbi - valor venal informado - total", "vvt_total_inf"));
                fiscal.setItbiPercentualVvt(getCellValueAsBigDecimal(row, headerMap, "itbi_percentual_vvt",
                        "itbi - percentual vvt (% venda)"));
                fiscal.setItbiAdquirenteNome(getCellValueAsString(row, headerMap, "itbi_adquirente_nome",
                        "itbi - adquirente principal - nome", "adquirentes_nomes", "adquirente"));
                fiscal.setItbiAdquirenteCpf(cleanDocumento(getCellValueAsString(row, headerMap, "itbi_adquirente_cpf",
                        "itbi - adquirente principal - cpf", "adquirentes_cpfs", "cpf adquirente")));

                // --- SEÇÃO VALOR VENAL - IPTU ---
                fiscal.setIptuValorVenalTerritorial(getCellValueAsBigDecimal(row, headerMap,
                        "iptu_valor_venal_territorial", "itbi - vvt iptu", "valor venal - territorial", "vvt_iptu"));
                fiscal.setIptuValorVenalPredial(getCellValueAsBigDecimal(row, headerMap, "iptu_valor_venal_predial",
                        "itbi - vvp iptu", "valor venal - predial", "vvp_iptu"));
                fiscal.setIptuValorVenalTotal(getCellValueAsBigDecimal(row, headerMap, "iptu_valor_venal_total",
                        "itbi - vv total iptu", "valor venal - total", "vvp_total_iptu"));

                // --- SEÇÃO CADIMO ---
                fiscal.setCadimoCadastro(getCellValueAsString(row, headerMap, "cadimo_cadastro", "cadimo - cadastro"));
                fiscal.setCadimoTipo(getCellValueAsString(row, headerMap, "cadimo_tipo", "cadimo - tipo"));
                fiscal.setCadimoSituacaoCadastral(getCellValueAsString(row, headerMap, "cadimo_situacao_cadastral",
                        "cadimo - situação cadastral"));
                fiscal.setCadimoInscricao(getCellValueAsString(row, headerMap, "cadimo_inscricao", "cadimo - inscrição",
                        "itbi - inscrição imobiliária", "inscrição", "inscricao"));
                fiscal.setCadimoQuadra(getCellValueAsString(row, headerMap, "cadimo_quadra", "cadimo - quadra"));
                fiscal.setCadimoLote(getCellValueAsString(row, headerMap, "cadimo_lote", "cadimo - lote"));
                fiscal.setCadimoBairroNome(
                        getCellValueAsString(row, headerMap, "cadimo_bairro_nome", "cadimo - bairro - nome"));
                fiscal.setCadimoLogradouroNome(
                        getCellValueAsString(row, headerMap, "cadimo_logradouro_nome", "cadimo - logradouro - nome"));
                fiscal.setCadimoNumero(getCellValueAsString(row, headerMap, "cadimo_numero", "cadimo - número"));
                fiscal.setCadimoComplemento(
                        getCellValueAsString(row, headerMap, "cadimo_complemento", "cadimo - complemento"));
                fiscal.setCadimoApartamentoUnidade(getCellValueAsString(row, headerMap, "cadimo_apartamento_unidade",
                        "cadimo - apartamento-unidade"));
                fiscal.setCadimoCep(getCellValueAsString(row, headerMap, "cadimo_cep", "cadimo - cep"));
                fiscal.setCadimoCodTerreno(
                        getCellValueAsString(row, headerMap, "cadimo_cod_terreno", "cadimo - cód. terreno"));
                fiscal.setCadimoProprietarioNome(getCellValueAsString(row, headerMap, "cadimo_proprietario_nome",
                        "cadimo - proprietário - nome/razão"));
                fiscal.setCadimoProprietarioCpfCnpj(cleanDocumento(getCellValueAsString(row, headerMap,
                        "cadimo_proprietario_cpf_cnpj", "cadimo - proprietário - cpf/cnpj")));
                fiscal.setCadimoResponsavelNome(getCellValueAsString(row, headerMap, "cadimo_responsavel_nome",
                        "cadimo - responsável - nome/razão"));
                fiscal.setCadimoResponsavelCpfCnpj(cleanDocumento(getCellValueAsString(row, headerMap,
                        "cadimo_responsavel_cpf_cnpj", "cadimo - responsável - cpf/cnpj")));
                fiscal.setCadimoAreaTerreno(getCellValueAsBigDecimal(row, headerMap, "cadimo_area_terreno",
                        "cadimo - área do terreno", "itbi - área terreno", "area_terreno"));
                fiscal.setCadimoAreaConstruida(getCellValueAsBigDecimal(row, headerMap, "cadimo_area_construida",
                        "cadimo - área construída", "itbi - área construída", "area_construida"));
                fiscal.setCadimoAreaTotalConstruida(getCellValueAsBigDecimal(row, headerMap,
                        "cadimo_area_total_construida", "cadimo - área total construída"));
                fiscal.setCadimoMatricula(getCellValueAsString(row, headerMap, "cadimo_matricula", "cadimo - matrícula",
                        "itbi - matrícula", "matricula"));
                fiscal.setCadimoCodigoNacionalMatricula(getCellValueAsString(row, headerMap,
                        "cadimo_codigo_nacional_matricula", "cadimo - código nacional de matrícula"));
                fiscal.setCadimoCib(
                        getCellValueAsString(row, headerMap, "cadimo_cib", "cadimo - cib", "itbi - cib", "cib"));
                fiscal.setCadimoNroImovelIncra(getCellValueAsString(row, headerMap, "cadimo_nro_imovel_incra",
                        "cadimo - nro do imóvel no incra"));
                fiscal.setCadimoNomePropriedade(getCellValueAsString(row, headerMap, "cadimo_nome_propriedade",
                        "cadimo - nome da propriedade"));

                // Tenta encontrar registro existente para Upsert
                Optional<FiscalItbi> existing = fiscalItbiRepository.findByItbiNumeroAndItbiAnoAndEntidade(
                        fiscal.getItbiNumero(), fiscal.getItbiAno(), entidade).stream().findFirst();

                if (existing.isPresent()) {
                    fiscal.setId(existing.get().getId());
                    fiscal.setCreatedAt(existing.get().getCreatedAt());
                    logger.debug("Atualizando registro ITBI: {}/{}", fiscal.getItbiNumero(), fiscal.getItbiAno());
                } else {
                    logger.debug("Inserindo novo registro ITBI: {}/{}", fiscal.getItbiNumero(), fiscal.getItbiAno());
                }

                fiscal.setImportacao(importacao);
                fiscalItbiRepository.save(fiscal);
                totalImportado++;

                if (totalImportado % 100 == 0) {
                    logger.info("Progresso da importação: {} registros processados...", totalImportado);
                    progressService.setProgress(entidadeId, totalImportado);
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        }
        if (totalImportado == 0) {
            String colunas = String.join(", ", headerMap.keySet());
            String dataMsg = firstLineData.isEmpty() ? "Nenhuma linha de dados encontrada."
                    : "Dados da 1ª linha lida: [" + String.join(" | ", firstLineData) + "]";
            throw new RuntimeException(
                    "Importação de 0 registros. Verifique 'Número' e 'Ano'. Colunas identificadas: [" + colunas + "]. "
                            + dataMsg);
        }

        importacao.setTotalRegistros(totalImportado);
        importacao.setStatus("CONCLUIDO");
        itbiImportacaoRepository.save(importacao);

        logger.info("Importação finalizada. Total importado: {}", totalImportado);
        progressService.setProgress(entidadeId, totalImportado);
        return totalImportado;
    }

    private String cleanDocumento(String doc) {
        if (doc == null)
            return null;
        return doc.replaceAll("\\D", "");
    }

    public Map<String, Object> getPreview(MultipartFile file) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> data = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        try (InputStream is = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = findBestSheet(workbook);
            Iterator<Row> rows = sheet.rowIterator();
            Map<String, Integer> headerMap = new HashMap<>();
            int rowCount = 0;

            // Busca cabeçalho
            while (rows.hasNext() && rowCount < 30) {
                Row currentRow = rows.next();
                rowCount++;
                List<String> currentHeaders = new ArrayList<>();
                boolean looksLikeHeader = false;

                for (Cell cell : currentRow) {
                    String val = getCellValueAsStringSafe(cell);
                    if (val != null && !val.trim().isEmpty()) {
                        String normalized = val.toLowerCase().trim();
                        currentHeaders.add(normalized);
                        if (normalized.contains("itbi") || normalized.contains("numero")
                                || normalized.contains("número")) {
                            looksLikeHeader = true;
                        }
                    }
                }

                if (looksLikeHeader && findColumnIndex(headerMap, "itbi - número", "itbi - numero", "numero") == null) {
                    for (int i = 0; i < currentRow.getLastCellNum(); i++) {
                        String h = getCellValueAsStringSafe(currentRow.getCell(i));
                        if (h != null) {
                            headerMap.put(h.toLowerCase().trim(), i);
                            columns.add(h);
                        }
                    }
                    if (findColumnIndex(headerMap, "itbi - número", "itbi - numero", "numero") != null)
                        break;
                }
            }

            // Pega primeiros 5 registros
            int count = 0;
            while (rows.hasNext() && count < 5) {
                Row row = rows.next();
                if (isRowEmpty(row))
                    continue;

                Map<String, String> rowMap = new HashMap<>();
                rowMap.put("numero", getCellValueAsString(row, headerMap, "itbi - número", "itbi - numero", "numero"));
                rowMap.put("ano", getCellValueAsString(row, headerMap, "itbi - ano", "ano"));
                rowMap.put("contribuinte", getCellValueAsString(row, headerMap, "itbi - proprietário principal - nome",
                        "itbi - adquirente principal - nome", "proprietario", "contribuinte"));

                data.add(rowMap);
                count++;
            }

            result.put("sheetName", sheet.getSheetName());
            result.put("columns", columns);
            result.put("records", data);
        }
        return result;
    }

    private String getCellValueAsString(Row row, Map<String, Integer> headerMap, String... aliases) {
        Integer colIndex = findColumnIndex(headerMap, aliases);
        if (colIndex == null)
            return null;
        return getCellValueAsStringSafe(row.getCell(colIndex));
    }

    private Integer getCellValueAsInteger(Row row, Map<String, Integer> headerMap, String... aliases) {
        Integer colIndex = findColumnIndex(headerMap, aliases);
        if (colIndex == null)
            return null;
        Cell cell = row.getCell(colIndex);
        if (cell == null)
            return null;

        CellType type = getCellTypeSafe(cell);
        if (type == CellType.NUMERIC)
            return (int) cell.getNumericCellValue();
        if (type == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private BigDecimal getCellValueAsBigDecimal(Row row, Map<String, Integer> headerMap, String... aliases) {
        Integer colIndex = findColumnIndex(headerMap, aliases);
        if (colIndex == null)
            return null;
        Cell cell = row.getCell(colIndex);
        if (cell == null)
            return null;

        CellType type = getCellTypeSafe(cell);
        if (type == CellType.NUMERIC)
            return BigDecimal.valueOf(cell.getNumericCellValue());
        if (type == CellType.STRING) {
            try {
                String val = cell.getStringCellValue().replace(",", ".").replaceAll("[^0-9.]", "");
                return new BigDecimal(val);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private LocalDate getCellValueAsLocalDate(Row row, Map<String, Integer> headerMap, String... aliases) {
        Integer colIndex = findColumnIndex(headerMap, aliases);
        if (colIndex == null)
            return null;
        Cell cell = row.getCell(colIndex);
        if (cell == null)
            return null;

        CellType type = getCellTypeSafe(cell);
        if (type == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        if (type == CellType.STRING) {
            try {
                String val = cell.getStringCellValue().trim();
                if (val.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    return LocalDate.parse(val, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private Integer findColumnIndex(Map<String, Integer> headerMap, String... aliases) {
        for (String alias : aliases) {
            String lowerAlias = alias.toLowerCase().trim();
            if (headerMap.containsKey(lowerAlias)) {
                return headerMap.get(lowerAlias);
            }
        }
        return null;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null)
            return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }

    private String getCellValueAsStringSafe(Cell cell) {
        if (cell == null)
            return null;
        CellType type = getCellTypeSafe(cell);
        if (type == CellType.STRING)
            return cell.getStringCellValue().trim();
        if (type == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            }
            double val = cell.getNumericCellValue();
            if (val == (long) val)
                return String.valueOf((long) val);
            return String.valueOf(val);
        }
        if (type == CellType.BOOLEAN)
            return String.valueOf(cell.getBooleanCellValue());
        return null;
    }

    private CellType getCellTypeSafe(Cell cell) {
        if (cell == null)
            return CellType.BLANK;
        return (cell.getCellType() == CellType.FORMULA) ? cell.getCachedFormulaResultType() : cell.getCellType();
    }

    private String getCellValueAsStringSimple(Row row, int index) {
        return getCellValueAsStringSafe(row.getCell(index));
    }

    private Sheet findBestSheet(Workbook workbook) {
        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++) {
            String name = workbook.getSheetName(i).toLowerCase().trim();
            if (name.contains("itbi") || name.contains("dados") || name.contains("lançamento")
                    || name.contains("lancamento")) {
                return workbook.getSheetAt(i);
            }
        }
        return workbook.getSheetAt(0);
    }
}
