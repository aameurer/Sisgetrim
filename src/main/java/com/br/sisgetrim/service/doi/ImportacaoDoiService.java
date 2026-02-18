package com.br.sisgetrim.service.doi;

import com.br.sisgetrim.dto.doi.*;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.doi.*;
import com.br.sisgetrim.repository.doi.*;
import com.br.sisgetrim.service.UsuarioService;
import com.br.sisgetrim.service.ImportProgressService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ImportacaoDoiService {

    private static final Logger logger = LoggerFactory.getLogger(ImportacaoDoiService.class);

    private final DoiImportacaoRepository importacaoRepository;
    private final DoiDeclaracaoRepository declaracaoRepository;
    private final DoiImportacaoErrosRepository errosRepository;
    private final UsuarioService usuarioService;
    private final ImportProgressService progressService;
    private final DoiLookupService lookupService;
    private final com.br.sisgetrim.repository.CartorioRepository cartorioRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ImportacaoDoiService(DoiImportacaoRepository importacaoRepository,
            DoiDeclaracaoRepository declaracaoRepository,
            DoiImportacaoErrosRepository errosRepository,
            UsuarioService usuarioService,
            ImportProgressService progressService,
            DoiLookupService lookupService,
            com.br.sisgetrim.repository.CartorioRepository cartorioRepository) {
        this.importacaoRepository = importacaoRepository;
        this.declaracaoRepository = declaracaoRepository;
        this.errosRepository = errosRepository;
        this.usuarioService = usuarioService;
        this.progressService = progressService;
        this.lookupService = lookupService;
        this.cartorioRepository = cartorioRepository;
    }

    @Transactional
    public DoiImportacao processarImportacao(DoiJsonDTO dto) {
        Usuario usuarioAuth = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = usuarioService.buscarPorDocumento(usuarioAuth.getDocumento());

        Entidade entidade = usuario.getEntidades().stream().findFirst().orElse(null);
        if (entidade == null) {
            throw new IllegalStateException("Usuário não está vinculado a nenhuma entidade (prefeitura).");
        }

        DoiImportacao importacao = new DoiImportacao();
        importacao.setEntidade(entidade);
        importacao.setUsuario(usuario);
        importacao.setNomeArquivo(
                dto.nomeArquivo() != null ? dto.nomeArquivo() : "Arquivo_Exportado_" + LocalDateTime.now());
        importacao.setStatus("PROCESSANDO");

        // Lógica de Vínculo de Cartório
        com.br.sisgetrim.model.Cartorio cartorioSelecionado = null;

        // 1. Se o usuário for de Cartório, pega o primeiro vinculado
        if (!usuario.getCartorios().isEmpty()) {
            cartorioSelecionado = usuario.getCartorios().stream().findFirst().orElse(null);
        }
        // 2. Se não for de Cartório (Admin/Prefeitura) e informou ID no DTO
        else if (dto.cartorioId() != null) {
            cartorioSelecionado = cartorioRepository.findById(dto.cartorioId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Cartório não encontrado com ID: " + dto.cartorioId()));
        }

        if (cartorioSelecionado != null) {
            // Valida se o cartório pertence à mesma entidade da importação
            if (!cartorioSelecionado.getEntidade().getId().equals(entidade.getId())) {
                throw new IllegalArgumentException("O Cartório selecionado não pertence à Entidade atual.");
            }
            importacao.setCartorio(cartorioSelecionado);
        } else {
            // Opcional: Bloquear se for obrigatório ter cartório
            // throw new IllegalArgumentException("É obrigatório selecionar um Cartório.");
        }

        importacao = importacaoRepository.save(importacao);

        if (dto.declaracoes() == null || dto.declaracoes().isEmpty()) {
            importacao.setStatus("ERRO");
            return importacaoRepository.save(importacao);
        }

        int count = 0;
        int linha = 0;
        for (DoiDeclaracaoDTO declDto : dto.declaracoes()) {
            linha++;
            StringBuilder logErros = new StringBuilder();

            String matricula = declDto.matricula() != null ? declDto.matricula() : "S/N-" + linha;

            // Revisão Completa: Verificar se a matrícula já existe para evitar erro de
            // duplicidade
            declaracaoRepository.findByMatricula(matricula).ifPresent(existente -> {
                logger.info("Removendo declaração pré-existente (PK: {}) com matrícula: {}", existente.getId(),
                        matricula);
                declaracaoRepository.delete(existente);
                // Flush para garantir a remoção antes do novo insert (devido ao cascade e
                // unique)
                entityManager.flush();
            });

            DoiDeclaracao declaracao = new DoiDeclaracao();
            declaracao.setImportacao(importacao);
            declaracao.setEntidade(entidade);
            declaracao.setMatricula(matricula);
            declaracao.setSituacao("RASCUNHO");

            try {
                if (declDto.dataLavraturaRegistroAverbacao() != null
                        && !declDto.dataLavraturaRegistroAverbacao().isEmpty()) {
                    declaracao.setDataLavratura(LocalDate.parse(declDto.dataLavraturaRegistroAverbacao()));
                } else {
                    declaracao.setDataLavratura(LocalDate.now()); // Fallback hoje
                }
            } catch (Exception e) {
                logErros.append("Erro ao processar data de lavratura; ");
                if (declaracao.getDataLavratura() == null) {
                    declaracao.setDataLavratura(LocalDate.now());
                }
            }

            // Dados de Operação Imobiliária (Estrutura DOI 2025)
            DoiOperacaoImobiliaria operacao = new DoiOperacaoImobiliaria();
            try {
                if (declDto.dataNegocioJuridico() != null && !declDto.dataNegocioJuridico().isEmpty()) {
                    operacao.setDataNegocioJuridico(LocalDate.parse(declDto.dataNegocioJuridico()));
                } else {
                    operacao.setDataNegocioJuridico(declaracao.getDataLavratura());
                }

                if (declDto.mesAnoUltimaParcela() != null && !declDto.mesAnoUltimaParcela().isEmpty()) {
                    try {
                        operacao.setMesAnoUltimaParcela(LocalDate.parse(declDto.mesAnoUltimaParcela()));
                    } catch (Exception e) {
                        logger.warn("Formato de data mesAnoUltimaParcela não padrão: {}",
                                declDto.mesAnoUltimaParcela());
                    }
                }
            } catch (Exception e) {
                logErros.append("Erro ao processar datas da operação; ");
                if (operacao.getDataNegocioJuridico() == null) {
                    operacao.setDataNegocioJuridico(declaracao.getDataLavratura());
                }
            }

            // Tradução De-Para Operação
            String tOp = lookupService.traduzir("dom_tipo_operacao", declDto.tipoOperacaoImobiliaria());
            operacao.setTipoOperacaoImobiliaria(tOp != null ? tOp : "OUTROS");

            String fPag = lookupService.traduzir("dom_forma_pagamento", declDto.formaPagamento());
            operacao.setFormaPagamento(fPag != null ? fPag : "A VISTA");

            operacao.setDescricaoOutrasOperacoes(declDto.descricaoOutrasOperacoesImobiliarias());
            operacao.setValorOperacaoImobiliaria(declDto.valorOperacaoImobiliaria());
            operacao.setIndicadorNaoConstaValorOperacao(
                    Boolean.TRUE.equals(declDto.indicadorNaoConstaValorOperacaoImobiliaria()));

            operacao.setValorBaseCalculoItbiItcmd(declDto.valorBaseCalculoItbiItcmd());
            operacao.setIndicadorNaoConstaBaseCalculo(
                    Boolean.TRUE.equals(declDto.indicadorNaoConstaValorBaseCalculoItbiItcmd()));

            operacao.setIndicadorAlienacaoFiduciaria(Boolean.TRUE.equals(declDto.indicadorAlienacaoFiduciaria()));
            operacao.setValorPagoAteDataAto(declDto.valorPagoAteDataAto());

            operacao.setIndicadorPermutaBens(Boolean.TRUE.equals(declDto.indicadorPermutaBens()));
            operacao.setIndicadorPagamentoDinheiro(Boolean.TRUE.equals(declDto.indicadorPagamentoDinheiro()));
            operacao.setValorPagoMoedaCorrenteDataAto(declDto.valorPagoMoedaCorrenteDataAto());

            // Blindagem contra campos nulos em colunas NOT NULL da base
            operacao.setTipoParteTransacionada(
                    declDto.tipoParteTransacionada() != null ? declDto.tipoParteTransacionada() : "INTEIRA");
            operacao.setValorParteTransacionada(
                    declDto.valorParteTransacionada() != null ? declDto.valorParteTransacionada() : BigDecimal.ZERO);

            // Validações de Domínio - Operação
            validarDominio(declDto.tipoOperacaoImobiliaria(), "dom_tipo_operacao", "Tipo da Operação", logErros);
            validarDominio(declDto.formaPagamento(), "dom_forma_pagamento", "Forma de Pagamento", logErros);

            // Vincula a operação à declaração
            declaracao.setOperacao(operacao);

            // Campos remanescentes na Declaração
            declaracao.setAreaImovel(declDto.areaImovel());
            declaracao.setCodigoIbge(declDto.codigoIbge());
            // declaracao.setMatricula(declDto.matricula() != null ? declDto.matricula() :
            // "S/N-" + linha); // Already set above

            // Validações de Domínio - Declaração
            validarDominio(declDto.tipoDeclaracao(), "dom_tipo_declaracao", "Tipo da Declaração", logErros);
            validarDominio(declDto.tipoServico(), "dom_tipo_servico", "Tipo do Serviço", logErros);
            validarDominio(declDto.tipoLivro(), "dom_tipo_livro", "Tipo do Livro", logErros);
            validarDominio(declDto.naturezaTitulo(), "dom_natureza_titulo", "Natureza do Título", logErros);
            validarDominio(declDto.tipoAto(), "dom_tipo_ato", "Tipo do Ato", logErros);

            // Novos campos Manual SQL na Declaração com Tradução De-Para
            String tDecl = lookupService.traduzir("dom_tipo_declaracao", declDto.tipoDeclaracao());
            declaracao.setTipoDeclaracao(tDecl != null ? tDecl : "Original");

            String tServ = lookupService.traduzir("dom_tipo_servico", declDto.tipoServico());
            declaracao.setTipoServico(tServ != null ? tServ : "N/A");

            String tAto = lookupService.traduzir("dom_tipo_ato", declDto.tipoAto());
            declaracao.setTipoAto(tAto != null ? tAto : "OUTROS");

            declaracao.setTipoLivro(lookupService.traduzir("dom_tipo_livro", declDto.tipoLivro()));
            declaracao.setNumeroLivro(declDto.numeroLivro());
            declaracao.setFolha(declDto.folha() != null ? declDto.folha() : "0");
            declaracao.setMneEletronica(declDto.matriculaNotarialEletronica());
            declaracao.setTranscricao(declDto.transcricao());
            declaracao.setCnmCodigoNacional(declDto.codigoNacionalMatricula());
            declaracao.setNumRegistroAverbacao(declDto.numeroRegistroAverbacao());
            declaracao.setNaturezaTitulo(lookupService.traduzir("dom_natureza_titulo", declDto.naturezaTitulo()));
            declaracao.setNumeroRegistroTd(declDto.numeroRegistro());
            declaracao.setExisteDoiAnterior(Boolean.TRUE.equals(declDto.existeDoiAnterior()));

            // Novos campos 2025 - Indicadores e Valores (Retificação Ato)
            declaracao.setRetificacaoAto(Boolean.TRUE.equals(declDto.retificacaoAto()));
            declaracao.setIndicadorImovelPublicoUniao(Boolean.TRUE.equals(declDto.indicadorImovelPublicoUniao()));
            declaracao.setIndicadorAreaConstruidaNaoConsta(
                    Boolean.TRUE.equals(declDto.indicadorAreaConstruidaNaoConsta()));
            declaracao.setIndicadorAreaLoteNaoConsta(Boolean.TRUE.equals(declDto.indicadorAreaLoteNaoConsta()));

            // Dados Detalhados do Imóvel (Estrutura DOI 2025)
            DoiImovel imovel = new DoiImovel();
            imovel.setEntidade(entidade);
            imovel.setCib(declDto.cib());
            imovel.setInscricaoMunicipal(declDto.inscricaoMunicipal());
            imovel.setAreaImovel(declDto.areaImovel() != null ? declDto.areaImovel() : BigDecimal.ZERO);
            imovel.setAreaConstruida(declDto.areaConstruida());

            // Novos campos Imóvel 2025
            imovel.setCertidaoAutorizacaoTransferenciaCat(declDto.certidaoAutorizacaoTransferencia());
            imovel.setCodigoIncra(declDto.codigoIncra());
            imovel.setMatricula(declDto.matricula());
            imovel.setTranscricao(declDto.transcricao());

            String tDest = lookupService.traduzir("dom_destinacao", declDto.destinacao());
            imovel.setDestinacao(tDest != null ? tDest : "URBANO");

            imovel.setRegistroImobiliarioPatrimonialRip(declDto.registroImobiliarioPatrimonial());
            imovel.setDenominacaoRural(declDto.denominacao());
            imovel.setLocalizacaoDetalhada(declDto.localizacao());
            imovel.setCep(declDto.cep() != null && !declDto.cep().isEmpty() ? declDto.cep() : "00000000");
            imovel.setBairro(declDto.bairro() != null && !declDto.bairro().isEmpty() ? declDto.bairro() : "N/A");
            imovel.setCodigoIbgeMunicipio(
                    declDto.codigoIbge() != null && !declDto.codigoIbge().isEmpty() ? declDto.codigoIbge() : "0000000");

            String tImov = lookupService.traduzir("dom_tipo_imovel", declDto.tipoImovel());
            imovel.setTipoImovel(tImov != null ? tImov : "N/A");

            imovel.setTipoLogradouro(declDto.tipoLogradouro() != null ? declDto.tipoLogradouro() : "N/A");
            imovel.setNomeLogradouro(declDto.nomeLogradouro() != null ? declDto.nomeLogradouro() : "N/A");
            imovel.setNumeroImovel(declDto.numeroImovel() != null ? declDto.numeroImovel() : "SN");
            imovel.setComplementoEndereco(declDto.complementoEndereco());
            imovel.setComplementoNumero(declDto.complementoNumeroImovel());

            // Validações de Domínio - Imóvel
            validarDominio(declDto.destinacao(), "dom_destinacao", "Destinação", logErros);
            validarDominio(declDto.tipoImovel(), "dom_tipo_imovel", "Tipo do Imóvel", logErros);

            imovel.setIndicadorImovelPublicoUniao(Boolean.TRUE.equals(declDto.indicadorImovelPublicoUniao()));
            imovel.setIndicadorAreaLoteNaoConsta(Boolean.TRUE.equals(declDto.indicadorAreaLoteNaoConsta()));
            imovel.setIndicadorAreaConstruidaNaoConsta(Boolean.TRUE.equals(declDto.indicadorAreaConstruidaNaoConsta()));

            if (declDto.municipiosUF() != null) {
                StringBuilder municipiosStr = new StringBuilder();
                declDto.municipiosUF().forEach(m -> {
                    if (municipiosStr.length() > 0)
                        municipiosStr.append(",");
                    municipiosStr.append(m.codigoIbge());
                });
                imovel.setMunicipiosUfLista(municipiosStr.toString());
            }

            declaracao.setDadosImovel(imovel);

            // Participantes e Validação de Participação
            processarParticipantes(declaracao, declDto, entidade, logErros);

            if (logErros.length() > 0) {
                declaracao.setSituacao("INAPTO");
                declaracao.setLogErros(logErros.toString());
                registrarErroDoc(importacao, linha, declaracao.getMatricula(), logErros.toString());
            }

            declaracaoRepository.save(declaracao);
            count++;

            if (count % 50 == 0) {
                entityManager.flush();
                entityManager.clear();
                progressService.setProgress(entidade.getId(), count);
            }
        }

        importacao.setTotalRegistros(count);
        importacao.setStatus("CONCLUIDO");
        return importacaoRepository.save(importacao);
    }

    private boolean processarParticipantes(DoiDeclaracao declaracao, DoiDeclaracaoDTO declDto, Entidade entidade,
            StringBuilder log) {
        BigDecimal totalAlienantes = BigDecimal.ZERO;
        BigDecimal totalAdquirentes = BigDecimal.ZERO;

        if (declDto.alienantes() != null) {
            for (DoiParticipanteDTO pDto : declDto.alienantes()) {
                // Novo: Salva na tabela específica de alienantes
                DoiAlienante alien = criarAlienante(pDto, entidade);
                declaracao.addAlienante(alien);

                if (alien.getParticipacao() != null)
                    totalAlienantes = totalAlienantes.add(alien.getParticipacao());
            }
        }

        if (declDto.adquirentes() != null) {
            for (DoiParticipanteDTO pDto : declDto.adquirentes()) {
                // Novo: Adquirente
                DoiAdquirente adq = criarAdquirente(pDto, entidade);
                declaracao.addAdquirente(adq);

                if (adq.getParticipacao() != null)
                    totalAdquirentes = totalAdquirentes.add(adq.getParticipacao());
            }
        }

        boolean alienanteOk = validarSoma(totalAlienantes);
        boolean adquirenteOk = validarSoma(totalAdquirentes);

        if (!alienanteOk)
            log.append("Soma de participação dos ALIENANTES inválida (").append(totalAlienantes).append("%); ");
        if (!adquirenteOk)
            log.append("Soma de participação dos ADQUIRENTES inválida (").append(totalAdquirentes).append("%); ");

        return alienanteOk && adquirenteOk;
    }

    private DoiAdquirente criarAdquirente(DoiParticipanteDTO pDto, Entidade entidade) {
        DoiAdquirente a = new DoiAdquirente();
        a.setEntidade(entidade);
        a.setNi(pDto.ni());
        a.setParticipacao(pDto.participacao());

        a.setIndicadorNiIdentificado(Boolean.TRUE.equals(pDto.indicadorNiIdentificado()));
        a.setMotivoNaoIdentificacaoNi(pDto.motivoNaoIdentificacaoNi());

        a.setIndicadorNaoConstaParticipacao(Boolean.TRUE.equals(pDto.indicadorNaoConstaParticipacaoOperacao()));
        a.setIndicadorEstrangeiro(Boolean.TRUE.equals(pDto.indicadorEstrangeiro()));
        a.setIndicadorEspolio(Boolean.TRUE.equals(pDto.indicadorEspolio()));
        a.setCpfInventariante(pDto.cpfInventariante());

        a.setIndicadorConjuge(Boolean.TRUE.equals(pDto.indicadorConjuge()));
        a.setIndicadorConjugeParticipa(pDto.indicadorConjugeParticipa());
        a.setRegimeBens(pDto.regimeBens());
        a.setIndicadorCpfConjugeIdentificado(pDto.indicadorCpfConjugeIdentificado());
        a.setCpfConjuge(pDto.cpfConjuge());

        a.setIndicadorRepresentante(Boolean.TRUE.equals(pDto.indicadorRepresentante()));
        if (pDto.representantes() != null) {
            a.setRepresentantes(pDto.representantes().stream().map(r -> r.ni()).toList());
        }

        return a;
    }

    private DoiAlienante criarAlienante(DoiParticipanteDTO pDto, Entidade entidade) {
        DoiAlienante a = new DoiAlienante();
        a.setEntidade(entidade);
        a.setNi(pDto.ni());
        a.setParticipacao(pDto.participacao());

        a.setIndicadorNiIdentificado(Boolean.TRUE.equals(pDto.indicadorNiIdentificado()));
        a.setMotivoNaoIdentificacaoNi(pDto.motivoNaoIdentificacaoNi());

        a.setIndicadorNaoConstaParticipacao(Boolean.TRUE.equals(pDto.indicadorNaoConstaParticipacaoOperacao()));
        a.setIndicadorEstrangeiro(Boolean.TRUE.equals(pDto.indicadorEstrangeiro()));
        a.setIndicadorEspolio(Boolean.TRUE.equals(pDto.indicadorEspolio()));
        a.setCpfInventariante(pDto.cpfInventariante());

        a.setIndicadorConjuge(Boolean.TRUE.equals(pDto.indicadorConjuge()));
        a.setIndicadorConjugeParticipa(pDto.indicadorConjugeParticipa());
        a.setRegimeBens(pDto.regimeBens());
        a.setIndicadorCpfConjugeIdentificado(pDto.indicadorCpfConjugeIdentificado());
        a.setCpfConjuge(pDto.cpfConjuge());

        a.setIndicadorRepresentante(Boolean.TRUE.equals(pDto.indicadorRepresentante()));
        if (pDto.representantes() != null) {
            a.setRepresentantes(pDto.representantes().stream().map(r -> r.ni()).toList());
        }

        return a;
    }

    private boolean validarSoma(BigDecimal soma) {
        return soma.compareTo(new BigDecimal("99.00")) >= 0 && soma.compareTo(new BigDecimal("100.01")) <= 0;
    }

    private void registrarErroDoc(DoiImportacao imp, int linha, String doc, String msg) {
        DoiImportacaoErros erro = new DoiImportacaoErros();
        erro.setImportacao(imp);
        erro.setLinhaJson(linha);
        erro.setCampoChave(doc);
        erro.setMensagemErro(msg);
        errosRepository.save(erro);
    }

    private void validarDominio(String valorStr, String dominio, String nomeAmigavel, StringBuilder log) {
        if (valorStr != null && !valorStr.isEmpty()) {
            try {
                Integer codigo = Integer.parseInt(valorStr);
                if (!lookupService.validar(dominio, codigo)) {
                    log.append("Código inválido para ").append(nomeAmigavel).append(": ").append(valorStr).append("; ");
                }
            } catch (NumberFormatException e) {
                log.append("Valor não numérico para ").append(nomeAmigavel).append(": ").append(valorStr).append("; ");
            }
        }
    }

    @Transactional(readOnly = true)
    public java.util.Optional<DoiDeclaracao> findByIdWithDetails(Long id) {
        return declaracaoRepository.findByIdWithDetails(id);
    }
}
