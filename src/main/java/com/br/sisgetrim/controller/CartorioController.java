package com.br.sisgetrim.controller;

import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.service.CartorioService;
import com.br.sisgetrim.service.UsuarioService;
import com.br.sisgetrim.model.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.br.sisgetrim.dto.CartorioRequestDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.br.sisgetrim.dto.CartorioResponseDTO;
import java.util.Set;

@Controller
@RequestMapping("/cartorios")
public class CartorioController {

    private final CartorioService cartorioService;
    private final UsuarioService usuarioService;

    @Autowired
    public CartorioController(CartorioService cartorioService, UsuarioService usuarioService) {
        this.cartorioService = cartorioService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(@AuthenticationPrincipal Usuario usuarioLogado, jakarta.servlet.http.HttpSession session,
            Model model) {
        Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);

        if (entidade != null) {
            model.addAttribute("cartorios", cartorioService.listarPorEntidade(entidade));
        }

        return "cartorios/listagem";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        var cartorioDto = new CartorioRequestDTO();
        cartorioDto.setAtribuicoes(Set.of());
        // Inicializa lista vazia. O usuário adiciona se quiser.
        cartorioDto.setResponsaveis(new java.util.ArrayList<>());

        model.addAttribute("cartorio", cartorioDto);

        carregarAtributos(model);
        return "cartorios/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado,
            jakarta.servlet.http.HttpSession session, Model model) {
        Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);

        try {
            CartorioResponseDTO cartorio = cartorioService.buscarPorIdEEntidade(id, entidade);
            model.addAttribute("cartorio", cartorio);
            carregarAtributos(model); // Reutiliza método auxiliar

            return "cartorios/cadastro";
        } catch (Exception e) {
            return "redirect:/cartorios";
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, @AuthenticationPrincipal Usuario usuarioLogado,
            jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);

        try {
            cartorioService.excluir(id, entidade);
            redirectAttributes.addFlashAttribute("sucesso", "Cartório excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir cartório: " + e.getMessage());
        }
        return "redirect:/cartorios";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("cartorio") CartorioRequestDTO dto,
            org.springframework.validation.BindingResult bindingResult,
            @AuthenticationPrincipal Usuario usuarioLogado,
            jakarta.servlet.http.HttpSession session,
            RedirectAttributes redirectAttributes,
            Model model) {

        System.out.println("=== TENTATIVA DE SALVAR CARTÓRIO ===");
        if (dto.getResponsaveis() != null) {
            // Remove nulos que podem ser gerados pelo binding em caso de "gaps" ou erros de
            // instanciação
            dto.getResponsaveis().removeIf(java.util.Objects::isNull);
        } else {
            dto.setResponsaveis(new java.util.ArrayList<>());
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("erro", "Por favor, corrija os erros destacados no formulário.");
            carregarAtributos(model);
            return "cartorios/cadastro";
        }

        try {
            Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);

            if (entidade == null) {
                throw new IllegalStateException("Usuário não possui uma entidade vinculada.");
            }

            if (dto.getId() != null) {
                cartorioService.atualizar(dto.getId(), dto, entidade);
                redirectAttributes.addFlashAttribute("sucesso", "Cartório atualizado com sucesso!");
            } else {
                cartorioService.cadastrar(dto, entidade);
                redirectAttributes.addFlashAttribute("sucesso", "Cartório cadastrado com sucesso!");
            }
            return "redirect:/cartorios";

        } catch (Exception e) {
            // Em caso de erro na lógica de negócio, recarregamos a página
            redirectAttributes.addFlashAttribute("erro", "Erro ao processar cartório: " + e.getMessage());
            // Se fosse erro de validação de negócio não capturado pelo @Valid,
            // poderíamos adicionar ao bindingResult ou model.
            // Para simplificar, redirect para GET de edição ou cadastro é mais seguro,
            // mas perde os dados digitados.
            // Melhor: retornar para a view com os dados.
            carregarAtributos(model);
            model.addAttribute("erro", e.getMessage());
            return "cartorios/cadastro";
        }
    }

    private void carregarAtributos(Model model) {
        model.addAttribute("situacoes", SituacaoCartorio.values());
        model.addAttribute("tipos", TipoCartorio.values());
        model.addAttribute("situacoesJuridicas", SituacaoJuridicaResponsavel.values());
        model.addAttribute("atribuicoes", AtribuicaoCartorio.values());
        model.addAttribute("tiposResponsavel", TipoResponsavel.values());
        model.addAttribute("statusSubstituto", StatusSubstituto.values());
    }
}
