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

        // 0. Tentar pegar entidade selecionada na sessão
        Long entidadeSelecionadaId = (Long) session.getAttribute("entidadeSelecionadaId");

        // 1. Tentar pegar do usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            try {
                Usuario usuarioAuth = (Usuario) auth.getPrincipal();
                // Recarregar para pegar a relação JPA
                Usuario usuario = usuarioService.buscarPorDocumento(usuarioAuth.getDocumento());

                // Disponibilizar lista de entidades para o modal de troca
                model.addAttribute("listaEntidadesUsuario", usuario.getEntidades());

                if (entidadeSelecionadaId != null) {
                    // Tenta encontrar a entidade selecionada na lista do usuário
                    com.br.sisgetrim.model.Entidade entidadeSelecionada = usuario.getEntidades().stream()
                            .filter(e -> e.getId().equals(entidadeSelecionadaId))
                            .findFirst()
                            .orElse(null);

                    if (entidadeSelecionada != null) {
                        entidadePrincipal = entidadeService.listarTodas().stream()
                                .filter(e -> e.id().equals(entidadeSelecionada.getId()))
                                .findFirst()
                                .orElse(null);
                    }
                }

                // Se não houver seleção na sessão ou for inválida, pega a primeira disponível
                if (entidadePrincipal == null) {
                    com.br.sisgetrim.model.Entidade entidade = usuario.getEntidades().stream().findFirst().orElse(null);
                    if (entidade != null) {
                        entidadePrincipal = entidadeService.listarTodas().stream()
                                .filter(e -> e.id().equals(entidade.getId()))
                                .findFirst()
                                .orElse(null);

                        // FIX: Persistir a seleção padrão na sessão para evitar trocas involuntárias
                        if (entidadePrincipal != null) {
                            session.setAttribute("entidadeSelecionadaId", entidadePrincipal.id());
                        }
                    }
                }
            } catch (Exception e) {
                // Silencioso se der erro no cast ou busca
            }
        }

        // 2. Fallback: Pegar a primeira se não houver vínculo
        if (entidadePrincipal == null) {
            entidadePrincipal = entidadeService.buscarPrimeira();
        }

        model.addAttribute("entidadePrincipal", entidadePrincipal);
    }
}
