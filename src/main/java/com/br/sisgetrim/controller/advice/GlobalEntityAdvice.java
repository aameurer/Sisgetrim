package com.br.sisgetrim.controller.advice;

import com.br.sisgetrim.dto.EntidadeResponseDTO;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.service.EntidadeService;
import com.br.sisgetrim.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalEntityAdvice {

    private final EntidadeService entidadeService;
    private final UsuarioService usuarioService;

    @Autowired
    private jakarta.servlet.http.HttpSession session;

    @Autowired
    public GlobalEntityAdvice(EntidadeService entidadeService, UsuarioService usuarioService) {
        this.entidadeService = entidadeService;
        this.usuarioService = usuarioService;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        EntidadeResponseDTO entidadePrincipal = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            try {
                Usuario usuarioAuth = (Usuario) auth.getPrincipal();
                // Recarregar para pegar a relação JPA
                Usuario usuario = usuarioService.buscarPorDocumento(usuarioAuth.getDocumento());

                // Disponibilizar lista de entidades para o modal de troca
                model.addAttribute("listaEntidadesUsuario", usuario.getEntidades());

                // Usa o serviço para pegar a entidade ativa (Sessão or First)
                com.br.sisgetrim.model.Entidade entidadeSelecionada = usuarioService.getEntidadeSelecionada(usuarioAuth,
                        session);

                if (entidadeSelecionada != null) {
                    entidadePrincipal = entidadeService.listarTodas().stream()
                            .filter(e -> e.id().equals(entidadeSelecionada.getId()))
                            .findFirst()
                            .orElse(null);
                }
            } catch (Exception e) {
                // Silencioso
            }
        }

        // 2. Fallback: Pegar a primeira se não houver vínculo
        if (entidadePrincipal == null) {
            entidadePrincipal = entidadeService.buscarPrimeira();
        }

        model.addAttribute("entidadePrincipal", entidadePrincipal);
    }
}
