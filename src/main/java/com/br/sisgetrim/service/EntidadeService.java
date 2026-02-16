package com.br.sisgetrim.service;

import com.br.sisgetrim.dto.EntidadeRequestDTO;
import com.br.sisgetrim.dto.EntidadeResponseDTO;
import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.repository.EntidadeRepository;
import com.br.sisgetrim.util.CnpjUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class EntidadeService {

    private final EntidadeRepository entidadeRepository;
    private final FileService fileService;

    @Autowired
    public EntidadeService(EntidadeRepository entidadeRepository, FileService fileService) {
        this.entidadeRepository = entidadeRepository;
        this.fileService = fileService;
    }

    @Transactional
    public EntidadeResponseDTO salvar(EntidadeRequestDTO dto, MultipartFile logo) throws IOException {
        String cnpjLimpo = dto.cnpj().replaceAll("\\D", "");

        Entidade entidade = entidadeRepository.findByCnpj(cnpjLimpo).orElse(new Entidade());

        entidade.setCnpj(cnpjLimpo);
        entidade.setTipoEstabelecimento(dto.tipoEstabelecimento());
        entidade.setDataAbertura(dto.dataAbertura());
        entidade.setNomeEmpresarial(dto.nomeEmpresarial());
        entidade.setNomeFantasia(dto.nomeFantasia());
        entidade.setPorte(dto.porte());
        entidade.setCnaePrincipal(dto.cnaePrincipal());
        entidade.setCnaeSecundario(dto.cnaeSecundario());
        entidade.setNaturezaJuridica(dto.naturezaJuridica());
        entidade.setLogradouro(dto.logradouro());
        entidade.setNumero(dto.numero());
        entidade.setComplemento(dto.complemento());
        entidade.setCep(dto.cep());
        entidade.setBairro(dto.bairro());
        entidade.setMunicipio(dto.municipio());
        entidade.setUf(dto.uf());
        entidade.setEmail(dto.email());
        entidade.setTelefone(dto.telefone());
        entidade.setEnteFederativo(dto.enteFederativo());
        entidade.setSituacaoCadastral(dto.situacaoCadastral());
        entidade.setDataSituacaoCadastral(dto.dataSituacaoCadastral());
        entidade.setMotivoSituacaoCadastral(dto.motivoSituacaoCadastral());
        entidade.setSituacaoEspecial(dto.situacaoEspecial());
        entidade.setDataSituacaoEspecial(dto.dataSituacaoEspecial());

        if (logo != null && !logo.isEmpty()) {
            // Se já tinha logo, deleta a antiga
            if (entidade.getLogoUrl() != null) {
                fileService.deletar(entidade.getLogoUrl(), "entidades");
            }
            String nomeArquivo = fileService.salvar(logo, "entidades");
            entidade.setLogoUrl(nomeArquivo);
        }

        Entidade salvo = entidadeRepository.save(entidade);
        return toResponseDTO(salvo);
    }

    public java.util.List<EntidadeResponseDTO> listarTodas() {
        return entidadeRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public EntidadeRequestDTO buscarPorId(Long id) {
        if (id == null)
            return null;
        return entidadeRepository.findById(id)
                .map(this::toRequestDTO)
                .orElse(null);
    }

    public EntidadeRequestDTO buscarPorCnpj(String cnpj) {
        String cnpjLimpo = cnpj.replaceAll("\\D", "");
        return entidadeRepository.findByCnpj(cnpjLimpo)
                .map(this::toRequestDTO)
                .orElse(null);
    }

    public EntidadeResponseDTO buscarPrimeira() {
        return entidadeRepository.findAll().stream()
                .findFirst()
                .map(this::toResponseDTO)
                .orElse(null);
    }

    private EntidadeRequestDTO toRequestDTO(Entidade entidade) {
        return new EntidadeRequestDTO(
                CnpjUtils.format(entidade.getCnpj()),
                entidade.getTipoEstabelecimento(),
                entidade.getDataAbertura(),
                entidade.getNomeEmpresarial(),
                entidade.getNomeFantasia(),
                entidade.getPorte(),
                entidade.getCnaePrincipal(),
                entidade.getCnaeSecundario(),
                entidade.getNaturezaJuridica(),
                entidade.getLogradouro(),
                entidade.getNumero(),
                entidade.getComplemento(),
                entidade.getCep(),
                entidade.getBairro(),
                entidade.getMunicipio(),
                entidade.getUf(),
                entidade.getEmail(),
                entidade.getTelefone(),
                entidade.getEnteFederativo(),
                entidade.getSituacaoCadastral(),
                entidade.getDataSituacaoCadastral(),
                entidade.getMotivoSituacaoCadastral(),
                entidade.getSituacaoEspecial(),
                entidade.getDataSituacaoEspecial(),
                entidade.getLogoUrl());
    }

    private EntidadeResponseDTO toResponseDTO(Entidade entidade) {
        return new EntidadeResponseDTO(
                entidade.getId(),
                CnpjUtils.format(entidade.getCnpj()),
                entidade.getNomeEmpresarial(),
                entidade.getNomeFantasia(),
                entidade.getMunicipio(),
                entidade.getUf(),
                entidade.getLogoUrl(),
                entidade.isAtivo());
    }
}
