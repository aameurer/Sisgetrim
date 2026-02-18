package com.br.sisgetrim.controller;

import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/entidade")
public class EntidadeSelectionController {

    private final UsuarioService usuarioService;

    @Autowired
    public EntidadeSelectionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/selecionar/{id}")
    public ResponseEntity<?> selecionarEntidade(@PathVariable Long id,
            @AuthenticationPrincipal Usuario usuarioLogado,
            HttpSession session) {
        try {
            // Recarregar usuário para garantir integridade dos dados
            Usuario usuario = usuarioService.buscarPorDocumento(usuarioLogado.getDocumento());

            // Verificar se o usuário possui vínculo com a entidade solicitada
            boolean possuiVinculo = usuario.getEntidades().stream()
                    .anyMatch(e -> e.getId().equals(id));

            if (possuiVinculo) {
                // Armazena a seleção na sessão
                session.setAttribute("entidadeSelecionadaId", id);
                return ResponseEntity.ok(Map.of("message", "Entidade selecionada com sucesso"));
            } else {
                return ResponseEntity.status(403).body(Map.of("message", "Usuário não possui acesso a esta entidade"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Erro ao selecionar entidade: " + e.getMessage()));
        }
    }
}
