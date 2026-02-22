package com.br.sisgetrim.controller;

import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.model.ParametroMalha;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.service.MalhaFiscalCruzamentoService;
import com.br.sisgetrim.service.ParametroMalhaService;
import com.br.sisgetrim.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/parametros-malha")
public class ParametroMalhaController {

    private static final Logger log = LoggerFactory.getLogger(ParametroMalhaController.class);

    private final ParametroMalhaService parametroMalhaService;
    private final MalhaFiscalCruzamentoService cruzamentoService;
    private final UsuarioService usuarioService;

    public ParametroMalhaController(ParametroMalhaService parametroMalhaService,
            MalhaFiscalCruzamentoService cruzamentoService,
            UsuarioService usuarioService) {
        this.parametroMalhaService = parametroMalhaService;
        this.cruzamentoService = cruzamentoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String exibir(@AuthenticationPrincipal Usuario usuarioLogado,
            HttpSession session, Model model) {
        Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
        if (entidade == null) {
            return "redirect:/dashboard";
        }

        ParametroMalha parametro = parametroMalhaService.buscarOuCriarPadrao(entidade);
        model.addAttribute("parametro", parametro);
        return "admin/parametros-malha";
    }

    @PostMapping("/salvar")
    public String salvar(@AuthenticationPrincipal Usuario usuarioLogado,
            HttpSession session,
            @RequestParam(value = "dataInicial", required = false) String dataInicialStr,
            @RequestParam(value = "dataFinal", required = false) String dataFinalStr,
            @RequestParam(value = "diferencaBcDoi", required = false) String diferencaBcDoiStr,
            @RequestParam(value = "diferencaImpostoDoi", required = false) String diferencaImpostoDoiStr,
            @RequestParam(value = "percentualAbaixoVvi", required = false) String percentualAbaixoVviStr,
            @RequestParam(value = "percentualAbaixoImpostoDoi", required = false) String percentualAbaixoImpostoDoiStr,
            @RequestParam(value = "considerarIntegralizacaoCapital", required = false) String considerarIntegralizacaoCapitalStr,
            RedirectAttributes redirectAttributes) {

        Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
        if (entidade == null) {
            redirectAttributes.addFlashAttribute("erro", "Nenhuma entidade selecionada.");
            return "redirect:/admin/parametros-malha";
        }

        try {
            ParametroMalha parametro = parametroMalhaService.buscarOuCriarPadrao(entidade);

            if (dataInicialStr != null && !dataInicialStr.isBlank()) {
                parametro.setDataInicial(LocalDate.parse(dataInicialStr));
            }
            if (dataFinalStr != null && !dataFinalStr.isBlank()) {
                parametro.setDataFinal(LocalDate.parse(dataFinalStr));
            }

            parametro.setDiferencaBcDoi(parseBigDecimal(diferencaBcDoiStr, BigDecimal.ONE));
            parametro.setDiferencaImpostoDoi(parseBigDecimal(diferencaImpostoDoiStr, BigDecimal.ONE));
            parametro.setPercentualAbaixoVvi(parseDouble(percentualAbaixoVviStr, 1.0));
            parametro.setPercentualAbaixoImpostoDoi(parseDouble(percentualAbaixoImpostoDoiStr, 1.0));
            parametro.setConsiderarIntegralizacaoCapital(
                    considerarIntegralizacaoCapitalStr != null
                            && "on".equalsIgnoreCase(considerarIntegralizacaoCapitalStr));

            parametroMalhaService.salvar(parametro);
            log.info("Parâmetros salvos para entidade ID={}", entidade.getId());
            redirectAttributes.addFlashAttribute("sucesso", "Parâmetros salvos com sucesso!");

        } catch (Exception e) {
            log.error("Erro ao salvar parâmetros da malha", e);
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar: " + e.getMessage());
        }

        return "redirect:/admin/parametros-malha";
    }

    /**
     * Endpoint AJAX para gerar a malha fiscal.
     * Salva parâmetros, executa cruzamento e persiste resultados.
     */
    @PostMapping("/gerar-malha")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> gerarMalha(
            @AuthenticationPrincipal Usuario usuarioLogado,
            HttpSession session,
            @RequestParam(value = "dataInicial", required = false) String dataInicialStr,
            @RequestParam(value = "dataFinal", required = false) String dataFinalStr,
            @RequestParam(value = "diferencaBcDoi", required = false) String diferencaBcDoiStr,
            @RequestParam(value = "diferencaImpostoDoi", required = false) String diferencaImpostoDoiStr,
            @RequestParam(value = "percentualAbaixoVvi", required = false) String percentualAbaixoVviStr,
            @RequestParam(value = "percentualAbaixoImpostoDoi", required = false) String percentualAbaixoImpostoDoiStr,
            @RequestParam(value = "considerarIntegralizacaoCapital", required = false) String considerarIntegralizacaoCapitalStr) {

        Map<String, Object> response = new HashMap<>();

        Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
        if (entidade == null) {
            response.put("status", "erro");
            response.put("message", "Nenhuma entidade selecionada.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // Atualizar parâmetros antes do cruzamento
            ParametroMalha parametro = parametroMalhaService.buscarOuCriarPadrao(entidade);

            if (dataInicialStr != null && !dataInicialStr.isBlank()) {
                parametro.setDataInicial(LocalDate.parse(dataInicialStr));
            }
            if (dataFinalStr != null && !dataFinalStr.isBlank()) {
                parametro.setDataFinal(LocalDate.parse(dataFinalStr));
            }

            parametro.setDiferencaBcDoi(parseBigDecimal(diferencaBcDoiStr, BigDecimal.ONE));
            parametro.setDiferencaImpostoDoi(parseBigDecimal(diferencaImpostoDoiStr, BigDecimal.ONE));
            parametro.setPercentualAbaixoVvi(parseDouble(percentualAbaixoVviStr, 1.0));
            parametro.setPercentualAbaixoImpostoDoi(parseDouble(percentualAbaixoImpostoDoiStr, 1.0));
            parametro.setConsiderarIntegralizacaoCapital(
                    considerarIntegralizacaoCapitalStr != null
                            && "on".equalsIgnoreCase(considerarIntegralizacaoCapitalStr));

            parametroMalhaService.salvar(parametro);

            // Executar cruzamento com persistência
            com.br.sisgetrim.model.MalhaLote lote = cruzamentoService.executarCruzamento(
                    entidade, usuarioLogado, parametro);

            // Buscar resultados persistidos e converter para DTO
            var resultados = cruzamentoService.buscarResultadosPorLote(lote.getId());
            var dtos = cruzamentoService.convertToDTO(resultados);

            response.put("status", "ok");
            response.put("loteId", lote.getId());
            response.put("totalAnalisado", lote.getTotalAnalisado());
            response.put("total", lote.getTotalDivergencias());
            response.put("resultados", dtos);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Erro ao gerar malha fiscal", e);
            response.put("status", "erro");
            response.put("message", "Erro ao processar cruzamento: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private BigDecimal parseBigDecimal(String valor, BigDecimal fallback) {
        if (valor == null || valor.isBlank())
            return fallback;
        try {
            String limpo = valor.replace(".", "").replace(",", ".");
            if (!limpo.contains(".") && !valor.contains(",")) {
                limpo = valor;
            }
            return new BigDecimal(limpo);
        } catch (NumberFormatException e) {
            log.warn("Conversão falhou para '{}', usando fallback={}", valor, fallback);
            return fallback;
        }
    }

    private Double parseDouble(String valor, Double fallback) {
        if (valor == null || valor.isBlank())
            return fallback;
        try {
            String limpo = valor.replace(".", "").replace(",", ".");
            if (!limpo.contains(".") && !valor.contains(",")) {
                limpo = valor;
            }
            return Double.parseDouble(limpo);
        } catch (NumberFormatException e) {
            log.warn("Conversão falhou para '{}', usando fallback={}", valor, fallback);
            return fallback;
        }
    }
}
