package com.br.sisgetrim.controller;

import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    @Autowired
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario,
            @RequestParam String confirmarSenha,
            RedirectAttributes redirectAttributes) {
        try {
            // Validar se as senhas coincidem
            if (!usuario.getSenha().equals(confirmarSenha)) {
                redirectAttributes.addFlashAttribute("erro", "As senhas não coincidem!");
                return "redirect:/cadastro";
            }

            // Validar tamanho mínimo da senha
            if (usuario.getSenha().length() < 6) {
                redirectAttributes.addFlashAttribute("erro", "A senha deve ter no mínimo 6 caracteres!");
                return "redirect:/cadastro";
            }

            // Cadastrar o usuário
            usuarioService.cadastrarUsuario(usuario);

            redirectAttributes.addFlashAttribute("sucesso",
                    "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cadastro";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }
}
