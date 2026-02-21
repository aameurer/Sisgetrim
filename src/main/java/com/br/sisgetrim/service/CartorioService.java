package com.br.sisgetrim.service;

import com.br.sisgetrim.dto.*;
import com.br.sisgetrim.model.*;
import com.br.sisgetrim.repository.CartorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartorioService {

    private final CartorioRepository cartorioRepository;
    private final com.br.sisgetrim.mapper.UsuarioMapper usuarioMapper;

    @Autowired
    public CartorioService(CartorioRepository cartorioRepository, com.br.sisgetrim.mapper.UsuarioMapper usuarioMapper) {
        this.cartorioRepository = cartorioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional
    public CartorioResponseDTO cadastrar(CartorioRequestDTO dto, Entidade entidade) {
        if (dto == null || dto.getCodigoCns() == null) {
            throw new IllegalArgumentException("Dados do cartório inválidos.");
        }
        if (cartorioRepository.existsByCodigoCns(dto.getCodigoCns())) {
            throw new IllegalArgumentException("Já existe um cartório cadastrado com este código CNS.");
        }

        Cartorio cartorio = mapToEntity(dto);
        cartorio.setEntidade(entidade);

        Cartorio salvo = cartorioRepository.save(cartorio);
        return mapToResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<CartorioResponseDTO> listarTodos() {
        return cartorioRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CartorioResponseDTO> listarPorEntidade(Entidade entidade) {
        if (entidade == null)
            return List.of();
        return cartorioRepository.findByEntidade(entidade).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CartorioResponseDTO buscarPorIdEEntidade(Long id, Entidade entidade) {
        if (id == null)
            throw new IllegalArgumentException("ID não pode ser nulo.");
        Cartorio cartorio = cartorioRepository.findById(id)
                .filter(c -> c.getEntidade() != null && c.getEntidade().equals(entidade))
                .orElseThrow(() -> new IllegalArgumentException("Cartório não encontrado ou acesso negado."));
        return mapToResponseDTO(cartorio);
    }

    @Transactional
    public CartorioResponseDTO atualizar(Long id, CartorioRequestDTO dto, Entidade entidade) {
        if (id == null)
            throw new IllegalArgumentException("ID não pode ser nulo.");
        Cartorio cartorio = cartorioRepository.findById(id)
                .filter(c -> c.getEntidade() != null && c.getEntidade().equals(entidade))
                .orElseThrow(() -> new IllegalArgumentException("Cartório não encontrado ou acesso negado."));

        // Proteção CNS: Não permite alterar
        String cnsDb = cartorio.getCodigoCns() != null ? cartorio.getCodigoCns().replaceAll("\\D", "") : "";
        String cnsDto = dto.getCodigoCns() != null ? dto.getCodigoCns().replaceAll("\\D", "") : "";
        if (!cnsDb.equals(cnsDto)) {
            throw new IllegalArgumentException("O código CNS não pode ser alterado.");
        }

        dto.setCodigoCns(cartorio.getCodigoCns()); // Restaura o original puro

        updateEntityFromDTO(cartorio, dto);
        Cartorio atualizado = cartorioRepository.save(cartorio);
        return mapToResponseDTO(atualizado);
    }

    @Transactional
    public void excluir(Long id, Entidade entidade) {
        if (id == null)
            throw new IllegalArgumentException("ID não pode ser nulo.");
        Cartorio cartorio = cartorioRepository.findById(id)
                .filter(c -> c.getEntidade() != null && c.getEntidade().equals(entidade))
                .orElseThrow(() -> new IllegalArgumentException("Cartório não encontrado ou acesso negado."));
        cartorioRepository.delete(cartorio);
    }

    private Cartorio mapToEntity(CartorioRequestDTO dto) {
        Cartorio entity = new Cartorio();
        updateEntityFromDTO(entity, dto);
        return entity;
    }

    private void updateEntityFromDTO(Cartorio entity, CartorioRequestDTO dto) {
        entity.setCodigoCns(dto.getCodigoCns());
        entity.setDenominacao(dto.getDenominacao());
        entity.setDataCriacao(dto.getDataCriacao());
        entity.setSituacao(dto.getSituacao());
        entity.setTipo(dto.getTipo());
        entity.setSituacaoJuridicaResponsavel(dto.getSituacaoJuridicaResponsavel());
        entity.setAtribuicoes(dto.getAtribuicoes());
        entity.setBairro(dto.getBairro());
        entity.setCep(dto.getCep());
        entity.setEndereco(dto.getEndereco());
        entity.setNumero(dto.getNumero());
        entity.setTelefonePrincipal(dto.getTelefonePrincipal());
        entity.setTelefoneSecundario(dto.getTelefoneSecundario());
        entity.setEmail(dto.getEmail());
        entity.setCnpj(dto.getCnpj());
        entity.setRazaoSocial(dto.getRazaoSocial());
        entity.setAtividadePrincipal(dto.getAtividadePrincipal());

        // Atualizar responsáveis (Limpa e adiciona novos conforme DTO)
        entity.getResponsaveis().clear();
        if (dto.getResponsaveis() != null) {
            dto.getResponsaveis().forEach(respDto -> {
                ResponsavelCartorio resp = new ResponsavelCartorio();
                resp.setTipo(respDto.getTipo());
                resp.setNome(respDto.getNome());
                resp.setCpf(respDto.getCpf());
                resp.setDataNomeacao(respDto.getDataNomeacao());
                resp.setDataIngresso(respDto.getDataIngresso());
                resp.setSubstituto(respDto.getSubstituto());
                resp.setCartorio(entity);
                entity.getResponsaveis().add(resp);
            });
        }
    }

    @Transactional(readOnly = true)
    public CartorioRequestDTO buscarRequestDtoPorIdEEntidade(Long id, Entidade entidade) {
        if (id == null)
            throw new IllegalArgumentException("ID não pode ser nulo.");
        Cartorio cartorio = cartorioRepository.findById(id)
                .filter(c -> c.getEntidade() != null && c.getEntidade().equals(entidade))
                .orElseThrow(() -> new IllegalArgumentException("Cartório não encontrado ou acesso negado."));
        return mapToRequestDTO(cartorio);
    }

    private CartorioRequestDTO mapToRequestDTO(Cartorio entity) {
        CartorioRequestDTO dto = new CartorioRequestDTO();
        dto.setId(entity.getId());
        dto.setCodigoCns(entity.getCodigoCns());
        dto.setDenominacao(entity.getDenominacao());
        dto.setDataCriacao(entity.getDataCriacao());
        dto.setSituacao(entity.getSituacao());
        dto.setTipo(entity.getTipo());
        dto.setSituacaoJuridicaResponsavel(entity.getSituacaoJuridicaResponsavel());
        dto.setAtribuicoes(new java.util.HashSet<>(entity.getAtribuicoes()));
        dto.setBairro(entity.getBairro());
        dto.setCep(entity.getCep());
        dto.setEndereco(entity.getEndereco());
        dto.setNumero(entity.getNumero());
        dto.setTelefonePrincipal(entity.getTelefonePrincipal());
        dto.setTelefoneSecundario(entity.getTelefoneSecundario());
        dto.setEmail(entity.getEmail());
        dto.setCnpj(entity.getCnpj());
        dto.setRazaoSocial(entity.getRazaoSocial());
        dto.setAtividadePrincipal(entity.getAtividadePrincipal());

        List<ResponsavelCartorioRequestDTO> responsaveis = entity.getResponsaveis().stream()
                .map(resp -> new ResponsavelCartorioRequestDTO(
                        resp.getTipo(),
                        resp.getNome(),
                        resp.getCpf(),
                        resp.getDataNomeacao(),
                        resp.getDataIngresso(),
                        resp.getSubstituto()))
                .collect(Collectors.toList());
        dto.setResponsaveis(responsaveis);

        return dto;
    }

    private CartorioResponseDTO mapToResponseDTO(Cartorio entity) {
        List<ResponsavelCartorioResponseDTO> responsaveis = entity.getResponsaveis().stream()
                .map(resp -> new ResponsavelCartorioResponseDTO(
                        resp.getId(),
                        resp.getTipo(),
                        resp.getNome(),
                        resp.getCpf(),
                        resp.getDataNomeacao(),
                        resp.getDataIngresso(),
                        resp.getSubstituto()))
                .collect(Collectors.toList());

        return new CartorioResponseDTO(
                entity.getId(),
                entity.getCodigoCns(),
                entity.getDenominacao(),
                entity.getDataCriacao(),
                entity.getSituacao(),
                entity.getTipo(),
                entity.getSituacaoJuridicaResponsavel(),
                entity.getAtribuicoes(),
                entity.getBairro(),
                entity.getCep(),
                entity.getEndereco(),
                entity.getNumero(),
                entity.getTelefonePrincipal(),
                entity.getEmail(),
                entity.getCnpj(),
                entity.getRazaoSocial(),
                entity.getNaturezaJuridica(),
                responsaveis,
                entity.getUsuarios().stream()
                        .map(usuarioMapper::toResponseDTO)
                        .collect(Collectors.toList()));
    }
}
