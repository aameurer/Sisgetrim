package com.br.sisgetrim.controller;

import com.br.sisgetrim.dto.SenhaUpdateDTO;
import com.br.sisgetrim.dto.UsuarioUpdateDTO;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.br.sisgetrim.service.EntidadeService;
import com.br.sisgetrim.dto.EntidadeResponseDTO;
import java.util.List;

@Controller
@RequestMapping("/configuracoes")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final EntidadeService entidadeService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, EntidadeService entidadeService) {
        this.usuarioService = usuarioService;
        this.entidadeService = entidadeService;
    }

    @GetMapping
    public String configuracoes(@AuthenticationPrincipal Usuario usuarioLogado, Model model) {
        // Recarregar usuário para garantir dados atualizados
        Usuario usuario = usuarioService.buscarPorDocumento(usuarioLogado.getDocumento());

        model.addAttribute("usuario", usuario);

        // Buscar todas as entidades para o dropdown
        List<EntidadeResponseDTO> entidades = entidadeService.listarTodas();
        model.addAttribute("entidades", entidades);

        if (!model.containsAttribute("usuarioUpdate")) {
            Long entidadeId = (usuario.getEntidade() != null) ? usuario.getEntidade().getId() : null;
            model.addAttribute("usuarioUpdate", new UsuarioUpdateDTO(usuario.getNome(), usuario.getEmail(),
                    entidadeId, usuario.getTipoUsuario(), usuario.getRole()));
        }

        if (!model.containsAttribute("senhaUpdate")) {
            model.addAttribute("senhaUpdate", new SenhaUpdateDTO("", "", ""));
        }

        return "configuracoes";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@Valid @ModelAttribute("usuarioUpdate") UsuarioUpdateDTO dto,
            BindingResult bindingResult,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            @AuthenticationPrincipal Usuario usuarioLogado,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usuarioUpdate",
                    bindingResult);
            redirectAttributes.addFlashAttribute("usuarioUpdate", dto);
            redirectAttributes.addFlashAttribute("tab", "perfil");
            return "redirect:/configuracoes";
        }

        try {
            boolean isMaster = usuarioLogado.getRole().equals("ROLE_MASTER");
            usuarioService.atualizarPerfil(usuarioLogado.getDocumento(), dto, foto, isMaster);
            redirectAttributes.addFlashAttribute("sucessoPerfil", "Perfil atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erroPerfil", e.getMessage());
        }

        redirectAttributes.addFlashAttribute("tab", "perfil");
        return "redirect:/configuracoes";
    }

    @PostMapping("/senha")
    public String alterarSenha(@Valid @ModelAttribute("senhaUpdate") SenhaUpdateDTO dto,
            BindingResult bindingResult,
            @AuthenticationPrincipal Usuario usuarioLogado,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.senhaUpdate",
                    bindingResult);
            redirectAttributes.addFlashAttribute("senhaUpdate", dto);
            redirectAttributes.addFlashAttribute("tab", "senha");
            return "redirect:/configuracoes";
        }

        try {
            usuarioService.alterarSenha(usuarioLogado.getDocumento(), dto);
            redirectAttributes.addFlashAttribute("sucessoSenha", "Senha alterada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erroSenha", e.getMessage());
        }

        redirectAttributes.addFlashAttribute("tab", "senha");
        return "redirect:/configuracoes";
    }
}
