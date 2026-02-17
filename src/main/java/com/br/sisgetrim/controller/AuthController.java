package com.br.sisgetrim.controller;

import com.br.sisgetrim.dto.UsuarioRequestDTO;
import com.br.sisgetrim.service.UsuarioService;
import com.br.sisgetrim.service.EntidadeService;
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
    private final EntidadeService entidadeService;
    private final com.br.sisgetrim.repository.doi.DoiImportacaoRepository importacaoRepository;
    private final com.br.sisgetrim.repository.FiscalItbiImportacaoRepository itbiImportacaoRepository;

    @Autowired
    public AuthController(UsuarioService usuarioService, EntidadeService entidadeService,
            com.br.sisgetrim.repository.doi.DoiImportacaoRepository importacaoRepository,
            com.br.sisgetrim.repository.FiscalItbiImportacaoRepository itbiImportacaoRepository) {
        this.usuarioService = usuarioService;
        this.entidadeService = entidadeService;
        this.importacaoRepository = importacaoRepository;
        this.itbiImportacaoRepository = itbiImportacaoRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("usuarioRequest", new UsuarioRequestDTO("", "", "", java.util.Collections.emptyList(),
                java.util.Collections.emptyList(), "", "", "", "ROLE_USER"));
        model.addAttribute("entidades", entidadeService.listarTodas());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(@Valid @ModelAttribute("usuarioRequest") UsuarioRequestDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("entidades", entidadeService.listarTodas());
            return "cadastro";
        }

        try {
            usuarioService.cadastrarUsuario(dto);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Cadastro realizado com sucesso! Faça login para continuar.");
            return "redirect:/login";

        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.warn("Erro de validação ao cadastrar usuário: {}", e.getMessage());
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("entidades", entidadeService.listarTodas());
            return "cadastro";
        } catch (Exception e) {
            logger.error("ERRO CRÍTICO ao cadastrar usuário: ", e);
            model.addAttribute("erro",
                    "Ocorreu um erro inesperado. Tente novamente mais tarde. Erro: " + e.getClass().getSimpleName());
            model.addAttribute("entidades", entidadeService.listarTodas());
            return "cadastro";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @org.springframework.security.core.annotation.AuthenticationPrincipal com.br.sisgetrim.model.Usuario usuarioLogado,
            Model model) {
        com.br.sisgetrim.model.Usuario usuario = usuarioService.buscarPorDocumento(usuarioLogado.getDocumento());
        com.br.sisgetrim.model.Entidade entidade = usuario.getEntidades().stream().findFirst().orElse(null);

        if (entidade != null) {
            model.addAttribute("importacoes", importacaoRepository.findByEntidade(entidade));
            model.addAttribute("importacoesItbi",
                    itbiImportacaoRepository.findByEntidadeOrderByCreatedAtDesc(entidade));
        }

        model.addAttribute("totalUsuarios", usuarioService.contarTotalUsuarios());
        model.addAttribute("usuariosOnline", usuarioService.contarUsuariosOnline());

        return "dashboard";
    }
}
