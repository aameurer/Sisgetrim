package com.br.sisgetrim.service.doi;

import com.br.sisgetrim.dto.doi.DoiJsonDTO;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.model.doi.DoiDeclaracao;
import com.br.sisgetrim.model.doi.DoiImportacao;
import com.br.sisgetrim.model.doi.DoiParticipante;
import com.br.sisgetrim.repository.doi.DoiDeclaracaoRepository;
import com.br.sisgetrim.repository.doi.DoiImportacaoRepository;
import com.br.sisgetrim.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.br.sisgetrim.service.ImportProgressService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;

@Service
public class ImportacaoDoiService {

    private static final Logger logger = LoggerFactory.getLogger(ImportacaoDoiService.class);

    private final DoiImportacaoRepository importacaoRepository;
    private final DoiDeclaracaoRepository declaracaoRepository;
    private final UsuarioService usuarioService;
    private final ImportProgressService progressService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ImportacaoDoiService(DoiImportacaoRepository importacaoRepository,
            DoiDeclaracaoRepository declaracaoRepository,
            UsuarioService usuarioService,
            ImportProgressService progressService) {
        this.importacaoRepository = importacaoRepository;
        this.declaracaoRepository = declaracaoRepository;
        this.usuarioService = usuarioService;
        this.progressService = progressService;
    }

    @Transactional
    public DoiImportacao processarImportacao(DoiJsonDTO dto) {
        // 1. Obter contexto do usuário logado
        Usuario usuarioAuth = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = usuarioService.buscarPorDocumento(usuarioAuth.getDocumento());

        if (usuario.getEntidade() == null) {
            throw new IllegalStateException("Usuário não está vinculado a nenhuma entidade (prefeitura).");
        }

        logger.info("Iniciando importação DOI para entidade: {} (Arquivo: {})",
                usuario.getEntidade().getNomeEmpresarial(), dto.nomeArquivo());

        // 2. Criar cabeçalho da importação
        DoiImportacao importacao = new DoiImportacao();
        importacao.setEntidade(usuario.getEntidade());
        importacao.setUsuario(usuario);
        importacao.setNomeArquivo(dto.nomeArquivo() != null ? dto.nomeArquivo() : "Arquivo Desconhecido");
        importacao.setTotalRegistros(0);
        importacao.setStatus("PROCESSANDO");
        importacao.setDataImportacao(LocalDateTime.now());
        importacao = importacaoRepository.save(importacao);

        progressService.setProgress(usuario.getEntidade().getId(), 0);

        if (dto.declaracoes() == null || dto.declaracoes().isEmpty()) {
            logger.warn(
                    "AVISO: Nenhuma declaração encontrada no DTO para o arquivo: {}. Verifique a estrutura do JSON.",
                    dto.nomeArquivo());
            importacao.setStatus("CONCLUIDO_VAZIO");
            return importacaoRepository.save(importacao);
        }

        logger.info("SUCESSO: Recebidas {} declarações para processamento.", dto.declaracoes().size());

        int count = 0;
        for (var declDto : dto.declaracoes()) {
            DoiDeclaracao declaracao = new DoiDeclaracao();
            declaracao.setImportacao(importacao);
            declaracao.setEntidade(usuario.getEntidade());
            declaracao.setNumeroDeclaracao(declDto.numeroDeclaracao());
            declaracao.setDataOperacao(declDto.dataLavraturaRegistroAverbacao());
            declaracao.setValorOperacao(declDto.valorOperacaoImobiliaria());
            declaracao.setAreaImovel(declDto.areaImovel());
            declaracao.setIndicadorNaoConstaValor(declDto.indicadorNaoConstaValorOperacaoImobiliaria());
            declaracao.setTipoImovel(declDto.tipoImovel());
            declaracao.setMunicipioImovel(declDto.codigoIbge());

            // Regra de Negócio: Status e Revisão
            if (declDto.indicadorNaoConstaValorOperacaoImobiliaria()) {
                declaracao.setSituacao("REVISAO_MANUAL");
            } else {
                declaracao.setSituacao("RASCUNHO");
            }

            // Participantes
            if (declDto.alienantes() != null) {
                for (var pDto : declDto.alienantes()) {
                    declaracao.addParticipante(criarParticipante(pDto, "ALIENANTE"));
                }
            }
            if (declDto.adquirentes() != null) {
                for (var pDto : declDto.adquirentes()) {
                    declaracao.addParticipante(criarParticipante(pDto, "ADQUIRENTE"));
                }
            }

            declaracaoRepository.save(declaracao);
            count++;

            if (count % 100 == 0) {
                logger.debug("Progresso DOI: {} registros...", count);
                progressService.setProgress(usuario.getEntidade().getId(), count);
                entityManager.flush();
                entityManager.clear();
            }
        }

        importacao.setTotalRegistros(count);
        importacao.setStatus("CONCLUIDO");
        progressService.setProgress(usuario.getEntidade().getId(), count);
        return importacaoRepository.save(importacao);
    }

    private DoiParticipante criarParticipante(com.br.sisgetrim.dto.doi.DoiParticipanteDTO pDto, String tipo) {
        DoiParticipante p = new DoiParticipante();
        p.setDocumento(pDto.ni());
        p.setPercentualParticipacao(pDto.participacao());
        p.setTipoParticipante(tipo);
        return p;
    }
}
