package com.br.sisgetrim.service;

import com.br.sisgetrim.model.FiscalItbi;
import com.br.sisgetrim.repository.FiscalItbiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FiscalItbiService {

    private final FiscalItbiRepository fiscalItbiRepository;

    @Autowired
    public FiscalItbiService(FiscalItbiRepository fiscalItbiRepository) {
        this.fiscalItbiRepository = fiscalItbiRepository;
    }

    @Transactional
    public FiscalItbi atualizarFiscal(Long id, FiscalItbi form) {
        FiscalItbi entity = fiscalItbiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro fiscal não encontrado: " + id));

        // Atualizar Seção ITBI
        entity.setItbiNumero(form.getItbiNumero());
        entity.setItbiAno(form.getItbiAno());
        entity.setItbiData(form.getItbiData());
        entity.setItbiTipo(form.getItbiTipo());
        entity.setItbiSituacao(form.getItbiSituacao());

        // Atualizar Partes
        entity.setItbiProprietarioNome(form.getItbiProprietarioNome());
        entity.setItbiProprietarioCpf(form.getItbiProprietarioCpf());
        entity.setItbiTransmitenteNome(form.getItbiTransmitenteNome());
        entity.setItbiTransmitenteCpf(form.getItbiTransmitenteCpf());
        entity.setItbiAdquirenteNome(form.getItbiAdquirenteNome());
        entity.setItbiAdquirenteCpf(form.getItbiAdquirenteCpf());

        // Atualizar CADIMO
        entity.setCadimoInscricao(form.getCadimoInscricao());
        entity.setCadimoCadastro(form.getCadimoCadastro());
        entity.setCadimoSituacaoCadastral(form.getCadimoSituacaoCadastral());
        entity.setCadimoTipo(form.getCadimoTipo());
        entity.setCadimoQuadra(form.getCadimoQuadra());
        entity.setCadimoLote(form.getCadimoLote());
        entity.setCadimoMatricula(form.getCadimoMatricula());
        entity.setCadimoLogradouroNome(form.getCadimoLogradouroNome());
        entity.setCadimoNumero(form.getCadimoNumero());
        entity.setCadimoBairroNome(form.getCadimoBairroNome());
        entity.setCadimoCep(form.getCadimoCep());
        entity.setCadimoComplemento(form.getCadimoComplemento());
        entity.setCadimoApartamentoUnidade(form.getCadimoApartamentoUnidade());

        // Áreas
        entity.setCadimoAreaTerreno(form.getCadimoAreaTerreno());
        entity.setCadimoAreaConstruida(form.getCadimoAreaConstruida());
        entity.setCadimoAreaTotalConstruida(form.getCadimoAreaTotalConstruida());

        // Outros
        entity.setCadimoCodigoNacionalMatricula(form.getCadimoCodigoNacionalMatricula());
        entity.setCadimoCib(form.getCadimoCib());
        entity.setCadimoNroImovelIncra(form.getCadimoNroImovelIncra());
        entity.setCadimoNomePropriedade(form.getCadimoNomePropriedade());

        return fiscalItbiRepository.save(entity);
    }

    public Optional<FiscalItbi> findById(Long id) {
        return fiscalItbiRepository.findById(id);
    }
}
