package com.br.sisgetrim.controller;

import com.br.sisgetrim.dto.doi.DoiJsonDTO;
import com.br.sisgetrim.model.doi.DoiImportacao;
import com.br.sisgetrim.service.doi.ImportacaoDoiService;
import com.br.sisgetrim.service.FiscalItbiImportService;
import com.br.sisgetrim.service.UsuarioService;
import com.br.sisgetrim.service.ImportProgressService;
import com.br.sisgetrim.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/malha")
public class DoiController {

    private final ImportacaoDoiService importacaoDoiService;
    private final FiscalItbiImportService fiscalItbiImportService;
    private final UsuarioService usuarioService;
    private final ImportProgressService progressService;

    @Autowired
    public DoiController(ImportacaoDoiService importacaoDoiService,
            FiscalItbiImportService fiscalItbiImportService,
            UsuarioService usuarioService,
            ImportProgressService progressService) {
        this.importacaoDoiService = importacaoDoiService;
        this.fiscalItbiImportService = fiscalItbiImportService;
        this.usuarioService = usuarioService;
        this.progressService = progressService;
    }

    @PostMapping("/importar")
    public ResponseEntity<Map<String, Object>> importar(@RequestBody DoiJsonDTO dto) {
        try {
            DoiImportacao result = importacaoDoiService.processarImportacao(dto);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Importação realizada com sucesso!");
            response.put("data", Map.of(
                    "importacaoId", result.getId(),
                    "entidade", result.getEntidade().getNomeEmpresarial(),
                    "totalRegistros", result.getTotalRegistros(),
                    "statusProcessamento", result.getStatus()));

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException | IllegalStateException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            e.printStackTrace(); // Log completo no console do servidor
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Erro interno ao processar a importação: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/preview-excel")
    public ResponseEntity<Map<String, Object>> previewExcel(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> preview = fiscalItbiImportService.getPreview(file);
            preview.put("status", "success");
            return ResponseEntity.ok(preview);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Erro ao gerar preview: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/import-progress")
    public ResponseEntity<Map<String, Object>> getImportProgress(@RequestParam("entidadeId") Long entidadeId) {
        Map<String, Object> response = new HashMap<>();
        response.put("count", progressService.getProgress(entidadeId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/importar-excel")
    public ResponseEntity<Map<String, Object>> importarExcel(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            Usuario usuario = usuarioService.buscarPorDocumento(usuarioLogado.getDocumento());
            Long entidadeId = usuario.getEntidades().stream()
                    .map(com.br.sisgetrim.model.Entidade::getId)
                    .findFirst()
                    .orElse(null);
            int total = fiscalItbiImportService.importarExcel(file, entidadeId);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Importação de " + total + " registros realizada com sucesso!");
            response.put("total", total);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Erro ao importar arquivo Excel: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
