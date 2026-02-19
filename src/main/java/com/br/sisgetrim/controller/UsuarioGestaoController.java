package com.br.sisgetrim.controller;

import com.br.sisgetrim.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioGestaoController {

    private final UsuarioService usuarioService;
    private final com.br.sisgetrim.service.EntidadeService entidadeService;
    private final com.br.sisgetrim.service.CartorioService cartorioService;

    @Autowired
    public UsuarioGestaoController(UsuarioService usuarioService,
            com.br.sisgetrim.service.EntidadeService entidadeService,
            com.br.sisgetrim.service.CartorioService cartorioService) {
        this.usuarioService = usuarioService;
        this.entidadeService = entidadeService;
        this.cartorioService = cartorioService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String listarUsuarios(org.springframework.ui.Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String novoUsuarioForm(org.springframework.ui.Model model) {
        model.addAttribute("usuarioRequest",
                new com.br.sisgetrim.dto.UsuarioRequestDTO(null, null, null, null, null, null, null, null, null));
        model.addAttribute("entidades", entidadeService.listarTodas());
        model.addAttribute("cartorios", cartorioService.listarTodos());
        return "usuarios/novo";
    }

    @PostMapping("/novo")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String salvarUsuario(
            @jakarta.validation.Valid @org.springframework.web.bind.annotation.ModelAttribute("usuarioRequest") com.br.sisgetrim.dto.UsuarioRequestDTO dto,
            org.springframework.validation.BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            org.springframework.ui.Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("entidades", entidadeService.listarTodas());
            model.addAttribute("cartorios", cartorioService.listarTodos());
            return "usuarios/novo";
        }
        try {
            usuarioService.cadastrarUsuario(dto);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário criado com sucesso!");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao criar usuário: " + e.getMessage());
            model.addAttribute("entidades", entidadeService.listarTodas());
            model.addAttribute("cartorios", cartorioService.listarTodos());
            return "usuarios/novo";
        }
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String editarUsuarioForm(@PathVariable Long id, org.springframework.ui.Model model) {
        try {
            com.br.sisgetrim.model.Usuario usuario = usuarioService.buscarPorId(id);
            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarioEditado", usuario); // Alias for template compatibility
            model.addAttribute("usuarioUpdate", usuarioService.buscarUpdateDTO(id));
            model.addAttribute("entidades", entidadeService.listarTodas());
            model.addAttribute("cartorios", cartorioService.listarTodos());
            return "usuarios/editar";
        } catch (Exception e) {
            return "redirect:/admin/usuarios";
        }
    }

    @PostMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String atualizarUsuario(@PathVariable Long id,
            @jakarta.validation.Valid @org.springframework.web.bind.annotation.ModelAttribute("usuarioUpdate") com.br.sisgetrim.dto.UsuarioUpdateDTO dto,
            org.springframework.validation.BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "usuarios/editar";
        }
        try {
            usuarioService.atualizarUsuarioPorAdmin(id, dto);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar usuário: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/deletar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String deletarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletarUsuario(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover usuário: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/aprovar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MASTER')")
    public String aprovarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.aprovarUsuario(id);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Usuário aprovado com sucesso! O acesso ao sistema foi liberado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao aprovar usuário: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
