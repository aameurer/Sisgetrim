package com.br.sisgetrim.controller;

import com.br.sisgetrim.repository.doi.DoiImportacaoRepository;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.service.UsuarioService;
import com.br.sisgetrim.repository.FiscalItbiImportacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/malha")
public class MalhaFiscalWebController {

    private final DoiImportacaoRepository importacaoRepository;
    private final FiscalItbiImportacaoRepository itbiImportacaoRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public MalhaFiscalWebController(DoiImportacaoRepository importacaoRepository,
            FiscalItbiImportacaoRepository itbiImportacaoRepository,
            UsuarioService usuarioService) {
        this.importacaoRepository = importacaoRepository;
        this.itbiImportacaoRepository = itbiImportacaoRepository;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/importar")
    public String importarPage(@AuthenticationPrincipal Usuario usuarioLogado, Model model) {
        Usuario usuario = usuarioService.buscarPorDocumento(usuarioLogado.getDocumento());
        Entidade entidade = usuario.getEntidade();

        if (entidade != null) {
            model.addAttribute("importacoes", importacaoRepository.findByEntidade(entidade));
            model.addAttribute("importacoesItbi",
                    itbiImportacaoRepository.findByEntidadeOrderByCreatedAtDesc(entidade));
            model.addAttribute("entidade", entidade);
        }

        return "malha/importar";
    }
}
