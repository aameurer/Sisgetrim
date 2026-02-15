package com.br.sisgetrim.controller;

import com.br.sisgetrim.dto.UsuarioRequestDTO;
import com.br.sisgetrim.service.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
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
        model.addAttribute("usuarioRequest", new UsuarioRequestDTO("", "", "", "", ""));
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@Valid @ModelAttribute("usuarioRequest") UsuarioRequestDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "cadastro";
        }

        try {
            usuarioService.cadastrarUsuario(dto);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";

        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Erro ao cadastrar usuário: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/cadastro";
        } catch (Exception e) {
            logger.error("Erro inesperado ao cadastrar usuário", e);
            redirectAttributes.addFlashAttribute("erro", "Ocorreu um erro inesperado. Tente novamente mais tarde.");
            return "redirect:/cadastro";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }
}
