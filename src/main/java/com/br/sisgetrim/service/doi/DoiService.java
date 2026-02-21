package com.br.sisgetrim.service.doi;

import com.br.sisgetrim.model.doi.DoiDeclaracao;
import com.br.sisgetrim.model.doi.DoiImovel;
import com.br.sisgetrim.model.doi.DoiOperacaoImobiliaria;
import com.br.sisgetrim.repository.doi.DoiDeclaracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoiService {

    private final DoiDeclaracaoRepository declaracaoRepository;

    @Autowired
    public DoiService(DoiDeclaracaoRepository declaracaoRepository) {
        this.declaracaoRepository = declaracaoRepository;
    }

    @Transactional
    public DoiDeclaracao atualizarDeclaracao(Long id, DoiDeclaracao form) {
        DoiDeclaracao entity = declaracaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro DOI não encontrado: " + id));

        // 1. Atualizar Dados Gerais da Declaração
        if (form.getTipoDeclaracao() != null)
            entity.setTipoDeclaracao(form.getTipoDeclaracao());
        if (form.getTipoServico() != null)
            entity.setTipoServico(form.getTipoServico());
        if (form.getDataLavratura() != null)
            entity.setDataLavratura(form.getDataLavratura());
        if (form.getTipoAto() != null)
            entity.setTipoAto(form.getTipoAto());
        entity.setMatricula(form.getMatricula());
        entity.setNumRegistroAverbacao(form.getNumRegistroAverbacao());
        entity.setTranscricao(form.getTranscricao());
        entity.setMneEletronica(form.getMneEletronica());
        entity.setCnmCodigoNacional(form.getCnmCodigoNacional());
        entity.setNaturezaTitulo(form.getNaturezaTitulo());
        entity.setTipoLivro(form.getTipoLivro());
        entity.setNumeroLivro(form.getNumeroLivro());
        if (form.getFolha() != null)
            entity.setFolha(form.getFolha());

        // 2. Atualizar Dados do Imóvel
        if (form.getDadosImovel() != null && entity.getDadosImovel() != null) {
            DoiImovel imovelEntity = entity.getDadosImovel();
            DoiImovel imovelForm = form.getDadosImovel();

            if (imovelForm.getInscricaoMunicipal() != null)
                imovelEntity.setInscricaoMunicipal(imovelForm.getInscricaoMunicipal());
            if (imovelForm.getCib() != null)
                imovelEntity.setCib(imovelForm.getCib());
            if (imovelForm.getDestinacao() != null)
                imovelEntity.setDestinacao(imovelForm.getDestinacao());
            if (imovelForm.getTipoImovel() != null)
                imovelEntity.setTipoImovel(imovelForm.getTipoImovel());
            if (imovelForm.getTipoLogradouro() != null)
                imovelEntity.setTipoLogradouro(imovelForm.getTipoLogradouro());
            if (imovelForm.getNomeLogradouro() != null)
                imovelEntity.setNomeLogradouro(imovelForm.getNomeLogradouro());
            if (imovelForm.getNumeroImovel() != null)
                imovelEntity.setNumeroImovel(imovelForm.getNumeroImovel());
            imovelEntity.setComplementoNumero(imovelForm.getComplementoNumero());
            imovelEntity.setComplementoEndereco(imovelForm.getComplementoEndereco());
            if (imovelForm.getBairro() != null)
                imovelEntity.setBairro(imovelForm.getBairro());
            if (imovelForm.getCep() != null)
                imovelEntity.setCep(imovelForm.getCep());
            if (imovelForm.getAreaImovel() != null) {
                imovelEntity.setAreaImovel(imovelForm.getAreaImovel());
                // Sincronizar campo redundante na declaração
                entity.setAreaImovel(imovelForm.getAreaImovel());
            }
            imovelEntity.setAreaConstruida(imovelForm.getAreaConstruida());
            imovelEntity.setCodigoIncra(imovelForm.getCodigoIncra());
            imovelEntity.setRegistroImobiliarioPatrimonialRip(imovelForm.getRegistroImobiliarioPatrimonialRip());
            imovelEntity.setDenominacaoRural(imovelForm.getDenominacaoRural());
            imovelEntity.setLocalizacaoDetalhada(imovelForm.getLocalizacaoDetalhada());

            // Indicadores
            imovelEntity.setIndicadorImovelPublicoUniao(imovelForm.isIndicadorImovelPublicoUniao());
            imovelEntity.setIndicadorAreaLoteNaoConsta(imovelForm.isIndicadorAreaLoteNaoConsta());
            imovelEntity.setIndicadorAreaConstruidaNaoConsta(imovelForm.isIndicadorAreaConstruidaNaoConsta());
        }

        // 3. Atualizar Operação Imobiliária
        if (form.getOperacao() != null && entity.getOperacao() != null) {
            DoiOperacaoImobiliaria operacaoEntity = entity.getOperacao();
            DoiOperacaoImobiliaria operacaoForm = form.getOperacao();

            if (operacaoForm.getValorOperacaoImobiliaria() != null)
                operacaoEntity.setValorOperacaoImobiliaria(operacaoForm.getValorOperacaoImobiliaria());
            operacaoEntity.setValorBaseCalculoItbiItcmd(operacaoForm.getValorBaseCalculoItbiItcmd());
            if (operacaoForm.getFormaPagamento() != null)
                operacaoEntity.setFormaPagamento(operacaoForm.getFormaPagamento());
            if (operacaoForm.getDataNegocioJuridico() != null)
                operacaoEntity.setDataNegocioJuridico(operacaoForm.getDataNegocioJuridico());
            operacaoEntity.setValorPagoAteDataAto(operacaoForm.getValorPagoAteDataAto());
            operacaoEntity.setValorPagoMoedaCorrenteDataAto(operacaoForm.getValorPagoMoedaCorrenteDataAto());
            if (operacaoForm.getTipoParteTransacionada() != null)
                operacaoEntity.setTipoParteTransacionada(operacaoForm.getTipoParteTransacionada());
            operacaoEntity.setValorParteTransacionada(operacaoForm.getValorParteTransacionada());
            operacaoEntity.setMesAnoUltimaParcela(operacaoForm.getMesAnoUltimaParcela());
            if (operacaoForm.getTipoOperacaoImobiliaria() != null)
                operacaoEntity.setTipoOperacaoImobiliaria(operacaoForm.getTipoOperacaoImobiliaria());
            operacaoEntity.setDescricaoOutrasOperacoes(operacaoForm.getDescricaoOutrasOperacoes());

            // Indicadores
            operacaoEntity.setIndicadorNaoConstaValorOperacao(operacaoForm.getIndicadorNaoConstaValorOperacao());
            operacaoEntity.setIndicadorNaoConstaBaseCalculo(operacaoForm.getIndicadorNaoConstaBaseCalculo());
            operacaoEntity.setIndicadorAlienacaoFiduciaria(operacaoForm.getIndicadorAlienacaoFiduciaria());
            operacaoEntity.setIndicadorPermutaBens(operacaoForm.getIndicadorPermutaBens());
            operacaoEntity.setIndicadorPagamentoDinheiro(operacaoForm.getIndicadorPagamentoDinheiro());
        }

        return declaracaoRepository.save(entity);
    }
}
