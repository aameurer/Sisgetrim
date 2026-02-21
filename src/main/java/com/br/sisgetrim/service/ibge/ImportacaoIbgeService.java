package com.br.sisgetrim.service.ibge;

import com.br.sisgetrim.dto.ibge.IbgeDeclaracaoDTO;
import com.br.sisgetrim.dto.ibge.IbgeImportRequestDTO;
import com.br.sisgetrim.dto.ibge.IbgePessoaDTO;
import com.br.sisgetrim.model.Cartorio;
import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.model.ibge.IbgeAdquirente;
import com.br.sisgetrim.model.ibge.IbgeAlienante;
import com.br.sisgetrim.model.ibge.IbgeDeclaracao;
import com.br.sisgetrim.model.ibge.IbgeImportacao;
import com.br.sisgetrim.repository.CartorioRepository;
import com.br.sisgetrim.repository.ibge.IbgeImportacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class ImportacaoIbgeService {

    private final IbgeImportacaoRepository ibgeImportacaoRepository;
    private final CartorioRepository cartorioRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    public ImportacaoIbgeService(IbgeImportacaoRepository ibgeImportacaoRepository,
            CartorioRepository cartorioRepository) {
        this.ibgeImportacaoRepository = ibgeImportacaoRepository;
        this.cartorioRepository = cartorioRepository;
    }

    @Transactional
    public IbgeImportacao processarImportacao(IbgeImportRequestDTO request, Entidade entidade, Usuario usuario) {
        if (request == null || request.getDeclaracoes() == null || request.getDeclaracoes().isEmpty()) {
            throw new IllegalArgumentException("O arquivo não contém declarações válidas.");
        }

        IbgeImportacao importacao = new IbgeImportacao();
        importacao.setEntidade(entidade);
        importacao.setUsuario(usuario);
        importacao.setNomeArquivo(request.getNomeArquivo());
        importacao.setDataImportacao(LocalDateTime.now());
        importacao.setStatus("CONCLUIDO");
        importacao.setTotalRegistros(request.getDeclaracoes().size());

        if (request.getCartorioId() != null) {
            Cartorio cartorio = cartorioRepository.findById(request.getCartorioId())
                    .orElseThrow(() -> new IllegalArgumentException("Cartório não encontrado."));
            importacao.setCartorio(cartorio);
        }

        for (IbgeDeclaracaoDTO dto : request.getDeclaracoes()) {
            IbgeDeclaracao declaracao = new IbgeDeclaracao();
            declaracao.setEntidade(entidade);
            // Associar com a Importacao
            importacao.addDeclaracao(declaracao);

            declaracao.setNomeCartorio(dto.getNomeCartorio());
            declaracao.setCns(dto.getCns());
            declaracao.setTipoServico(dto.getTipoServico());
            declaracao.setTipoDeclaracao(dto.getTipoDeclaracao());
            declaracao.setMatricula(dto.getMatricula());
            declaracao.setTranscricao(dto.getTranscricao());
            declaracao.setCodigoNacionalMatricula(dto.getCodigoNacionalMatricula());
            declaracao.setMatriculaNotarialEletronica(dto.getMatriculaNotarialEletronica());
            declaracao.setTipoOperacaoImobiliaria(dto.getTipoOperacaoImobiliaria());
            declaracao.setDescricaoOutrasOperacoesImobiliarias(dto.getDescricaoOutrasOperacoesImobiliarias());

            // Tratamento de Data
            try {
                if (dto.getDataLavraturaRegistroAverbacao() != null
                        && !dto.getDataLavraturaRegistroAverbacao().trim().isEmpty()) {
                    LocalDate data = LocalDate.parse(dto.getDataLavraturaRegistroAverbacao(), formatter);
                    declaracao.setDataLavraturaRegistroAverbacao(data);
                }
            } catch (DateTimeParseException e) {
                // Fallback ou exceção silenciosa
                throw new IllegalArgumentException(
                        "Formato de data inválido na declaração da matrícula " + dto.getMatricula());
            }

            declaracao.setDestinacao(dto.getDestinacao());
            declaracao.setTipoLogradouro(dto.getTipoLogradouro());
            declaracao.setNomeLogradouro(dto.getNomeLogradouro());
            declaracao.setNumeroImovel(dto.getNumeroImovel());
            declaracao.setComplementoEndereco(dto.getComplementoEndereco());
            declaracao.setComplementoNumeroImovel(dto.getComplementoNumeroImovel());
            declaracao.setBairro(dto.getBairro());
            declaracao.setInscricaoMunicipal(dto.getInscricaoMunicipal());
            declaracao.setCodigoIbge(dto.getCodigoIbge());
            declaracao.setDenominacao(dto.getDenominacao());
            declaracao.setLocalizacao(dto.getLocalizacao());
            declaracao.setCib(dto.getCib());

            // Processar Alienantes
            if (dto.getAlienantes() != null && !dto.getAlienantes().isEmpty()) {
                System.out.println("Processando " + dto.getAlienantes().size() + " Alienantes para matricula "
                        + dto.getMatricula());
                for (IbgePessoaDTO pessoaDTO : dto.getAlienantes()) {
                    IbgeAlienante alienante = new IbgeAlienante();
                    alienante.setIndicadorNiIdentificado(
                            pessoaDTO.getIndicadorNiIdentificado() != null ? pessoaDTO.getIndicadorNiIdentificado()
                                    : false);
                    alienante.setNi(pessoaDTO.getNi());
                    alienante.setMotivoNaoIdentificacaoNi(pessoaDTO.getMotivoNaoIdentificacaoNi());
                    alienante.setIndicadorCpfConjugeIdentificado(pessoaDTO.getIndicadorCpfConjugeIdentificado() != null
                            ? pessoaDTO.getIndicadorCpfConjugeIdentificado()
                            : false);
                    alienante.setCpfConjuge(pessoaDTO.getCpfConjuge());
                    declaracao.addAlienante(alienante); // Helper method
                }
            } else {
                System.out.println("Sem Alienantes para matricula " + dto.getMatricula());
            }

            // Processar Adquirentes
            if (dto.getAdquirentes() != null && !dto.getAdquirentes().isEmpty()) {
                System.out.println("Processando " + dto.getAdquirentes().size() + " Adquirentes para matricula "
                        + dto.getMatricula());
                for (IbgePessoaDTO pessoaDTO : dto.getAdquirentes()) {
                    IbgeAdquirente adquirente = new IbgeAdquirente();
                    adquirente.setIndicadorNiIdentificado(
                            pessoaDTO.getIndicadorNiIdentificado() != null ? pessoaDTO.getIndicadorNiIdentificado()
                                    : false);
                    adquirente.setNi(pessoaDTO.getNi());
                    adquirente.setMotivoNaoIdentificacaoNi(pessoaDTO.getMotivoNaoIdentificacaoNi());
                    adquirente.setIndicadorCpfConjugeIdentificado(pessoaDTO.getIndicadorCpfConjugeIdentificado() != null
                            ? pessoaDTO.getIndicadorCpfConjugeIdentificado()
                            : false);
                    adquirente.setCpfConjuge(pessoaDTO.getCpfConjuge());
                    declaracao.addAdquirente(adquirente); // Helper method
                }
            } else {
                System.out.println("Sem Adquirentes para matricula " + dto.getMatricula());
            }
        }

        // Salvar batch de Importacao completa (cascades Declaracao -> (Alienante &
        // Adquirente))
        return ibgeImportacaoRepository.save(importacao);
    }
}
