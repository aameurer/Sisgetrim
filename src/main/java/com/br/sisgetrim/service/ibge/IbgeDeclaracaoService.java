package com.br.sisgetrim.service.ibge;

import com.br.sisgetrim.model.ibge.IbgeDeclaracao;
import com.br.sisgetrim.repository.ibge.IbgeDeclaracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IbgeDeclaracaoService {

    private final IbgeDeclaracaoRepository ibgeDeclaracaoRepository;

    @Autowired
    public IbgeDeclaracaoService(IbgeDeclaracaoRepository ibgeDeclaracaoRepository) {
        this.ibgeDeclaracaoRepository = ibgeDeclaracaoRepository;
    }

    @Transactional
    public IbgeDeclaracao atualizarDeclaracao(Long id, IbgeDeclaracao ibgeForm) {
        IbgeDeclaracao declaracao = ibgeDeclaracaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Declaração não encontrada: " + id));

        // Atualizar campos básicos
        declaracao.setNomeCartorio(ibgeForm.getNomeCartorio());
        declaracao.setCns(ibgeForm.getCns());
        declaracao.setTipoServico(ibgeForm.getTipoServico());
        declaracao.setTipoDeclaracao(ibgeForm.getTipoDeclaracao());
        declaracao.setMatricula(ibgeForm.getMatricula());
        declaracao.setTranscricao(ibgeForm.getTranscricao());
        declaracao.setCodigoNacionalMatricula(ibgeForm.getCodigoNacionalMatricula());
        declaracao.setMatriculaNotarialEletronica(ibgeForm.getMatriculaNotarialEletronica());
        declaracao.setTipoOperacaoImobiliaria(ibgeForm.getTipoOperacaoImobiliaria());
        declaracao.setDescricaoOutrasOperacoesImobiliarias(ibgeForm.getDescricaoOutrasOperacoesImobiliarias());
        declaracao.setDataLavraturaRegistroAverbacao(ibgeForm.getDataLavraturaRegistroAverbacao());
        declaracao.setDestinacao(ibgeForm.getDestinacao());

        // Endereço e Localização
        declaracao.setTipoLogradouro(ibgeForm.getTipoLogradouro());
        declaracao.setNomeLogradouro(ibgeForm.getNomeLogradouro());
        declaracao.setNumeroImovel(ibgeForm.getNumeroImovel());
        declaracao.setComplementoEndereco(ibgeForm.getComplementoEndereco());
        declaracao.setComplementoNumeroImovel(ibgeForm.getComplementoNumeroImovel());
        declaracao.setBairro(ibgeForm.getBairro());
        declaracao.setInscricaoMunicipal(ibgeForm.getInscricaoMunicipal());
        declaracao.setCodigoIbge(ibgeForm.getCodigoIbge());
        declaracao.setDenominacao(ibgeForm.getDenominacao());
        declaracao.setLocalizacao(ibgeForm.getLocalizacao());
        declaracao.setCib(ibgeForm.getCib());

        // Atualizar Alienantes e Adquirentes (Sync NI)
        if (ibgeForm.getAlienantes() != null) {
            for (int i = 0; i < ibgeForm.getAlienantes().size(); i++) {
                if (i < declaracao.getAlienantes().size()) {
                    declaracao.getAlienantes().get(i)
                            .setNi(ibgeForm.getAlienantes().get(i).getNi());
                }
            }
        }
        if (ibgeForm.getAdquirentes() != null) {
            for (int i = 0; i < ibgeForm.getAdquirentes().size(); i++) {
                if (i < declaracao.getAdquirentes().size()) {
                    declaracao.getAdquirentes().get(i)
                            .setNi(ibgeForm.getAdquirentes().get(i).getNi());
                }
            }
        }

        return ibgeDeclaracaoRepository.save(declaracao);
    }
}
