package com.br.sisgetrim.controller;

import com.br.sisgetrim.dto.EntidadeRequestDTO;
import com.br.sisgetrim.service.EntidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Controller
@RequestMapping("/entidades")
public class EntidadeController {

    private final EntidadeService entidadeService;

    @Autowired
    public EntidadeController(EntidadeService entidadeService) {
        this.entidadeService = entidadeService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("entidades", entidadeService.listarTodas());
        return "entidades/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        if (!model.containsAttribute("entidadeRequest")) {
            model.addAttribute("entidadeRequest", new EntidadeRequestDTO(
                    "", "", null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "ATIVA",
                    java.time.LocalDate.now(),
                    "", "", null, null));
        }
        return "entidades/cadastro";
    }

    @GetMapping("/editar/id/{id}")
    public String editarPorId(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        EntidadeRequestDTO dto = entidadeService.buscarPorId(id);
        if (dto == null) {
            redirectAttributes.addFlashAttribute("erro", "Entidade não encontrada.");
            return "redirect:/entidades";
        }
        model.addAttribute("entidadeRequest", dto);
        return "entidades/cadastro";
    }

    @GetMapping("/editar/{cnpj}")
    public String editar(@PathVariable String cnpj, Model model, RedirectAttributes redirectAttributes) {
        EntidadeRequestDTO dto = entidadeService.buscarPorCnpj(cnpj);
        if (dto == null) {
            redirectAttributes.addFlashAttribute("erro", "Entidade não encontrada para o CNPJ informado.");
            return "redirect:/entidades/novo";
        }
        model.addAttribute("entidadeRequest", dto);
        return "entidades/cadastro";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            entidadeService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Entidade excluída com sucesso!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir entidade: " + e.getMessage());
        }
        return "redirect:/entidades";
    }

    @PostMapping("/novo")
    public String salvar(@Valid @ModelAttribute("entidadeRequest") EntidadeRequestDTO dto,
            BindingResult bindingResult,
            @RequestParam(value = "logo", required = false) MultipartFile logo,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "entidades/cadastro";
        }

        try {
            entidadeService.salvar(dto, logo);
            redirectAttributes.addFlashAttribute("sucesso", "Dados da entidade gravados com sucesso!");
            return "redirect:/entidades";
        } catch (IOException e) {
            model.addAttribute("erro", "Erro ao salvar a logo: " + e.getMessage());
            return "entidades/cadastro";
        } catch (Exception e) {
            model.addAttribute("erro", "Ocorreu um erro inesperado: " + e.getMessage());
            return "entidades/cadastro";
        }
    }
}
