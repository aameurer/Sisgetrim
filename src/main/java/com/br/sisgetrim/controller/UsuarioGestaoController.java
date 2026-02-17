package com.br.sisgetrim.controller;

import com.br.sisgetrim.dto.UsuarioUpdateDTO;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.service.EntidadeService;
import com.br.sisgetrim.service.CartorioService;
import com.br.sisgetrim.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
public class UsuarioGestaoController {

    private final UsuarioService usuarioService;
    private final EntidadeService entidadeService;
    private final CartorioService cartorioService;

    @Autowired
    public UsuarioGestaoController(UsuarioService usuarioService, EntidadeService entidadeService,
            CartorioService cartorioService) {
        this.usuarioService = usuarioService;
        this.entidadeService = entidadeService;
        this.cartorioService = cartorioService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String editarUsuario(@PathVariable Long id, Model model) {
        UsuarioUpdateDTO usuarioUpdate = usuarioService.buscarUpdateDTO(id);
        model.addAttribute("usuarioUpdate", usuarioUpdate);
        model.addAttribute("usuarioId", id);
        model.addAttribute("entidades", entidadeService.listarTodas());
        model.addAttribute("cartorios", cartorioService.listarTodos());

        Usuario usuarioEntity = usuarioService.buscarPorId(id);
        model.addAttribute("usuarioEditado", usuarioEntity);

        return "usuarios/editar";
    }

    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String salvarEdicao(@PathVariable Long id,
            @Valid @ModelAttribute("usuarioUpdate") UsuarioUpdateDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarioId", id);
            model.addAttribute("entidades", entidadeService.listarTodas());
            model.addAttribute("cartorios", cartorioService.listarTodos());
            return "usuarios/editar";
        }

        try {
            usuarioService.atualizarUsuarioPorAdmin(id, dto);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
            return "redirect:/usuarios";
        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuarioId", id);
            model.addAttribute("entidades", entidadeService.listarTodas());
            model.addAttribute("cartorios", cartorioService.listarTodos());
            return "usuarios/editar";
        }
    }

    @PostMapping("/deletar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletarUsuario(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao deletar usuário: " + e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
