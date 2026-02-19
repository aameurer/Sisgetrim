package com.br.sisgetrim.controller;

import com.br.sisgetrim.repository.doi.DoiImportacaoRepository;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.service.UsuarioService;
import com.br.sisgetrim.repository.FiscalItbiImportacaoRepository;
import com.br.sisgetrim.model.doi.DoiDeclaracao;
import com.br.sisgetrim.service.PdfService;
import com.br.sisgetrim.service.doi.ImportacaoDoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
@RequestMapping("/malha")
public class MalhaFiscalWebController {

    private final DoiImportacaoRepository importacaoRepository;
    private final FiscalItbiImportacaoRepository itbiImportacaoRepository;
    private final UsuarioService usuarioService;
    private final com.br.sisgetrim.repository.CartorioRepository cartorioRepository;
    private final com.br.sisgetrim.repository.doi.DoiDeclaracaoRepository doiDeclaracaoRepository;
    private final ImportacaoDoiService importacaoDoiService;
    private final PdfService pdfService;
    private final TemplateEngine templateEngine;

    @Autowired
    public MalhaFiscalWebController(DoiImportacaoRepository importacaoRepository,
            FiscalItbiImportacaoRepository itbiImportacaoRepository,
            UsuarioService usuarioService,
            com.br.sisgetrim.repository.CartorioRepository cartorioRepository,
            com.br.sisgetrim.repository.doi.DoiDeclaracaoRepository doiDeclaracaoRepository,
            ImportacaoDoiService importacaoDoiService,
            PdfService pdfService,
            TemplateEngine templateEngine) {
        this.importacaoRepository = importacaoRepository;
        this.itbiImportacaoRepository = itbiImportacaoRepository;
        this.usuarioService = usuarioService;
        this.cartorioRepository = cartorioRepository;
        this.doiDeclaracaoRepository = doiDeclaracaoRepository;
        this.importacaoDoiService = importacaoDoiService;
        this.pdfService = pdfService;
        this.templateEngine = templateEngine;
    }

    @GetMapping("/analise-importacoes")
    public String analiseImportacoes(@AuthenticationPrincipal Usuario usuarioLogado,
            jakarta.servlet.http.HttpSession session,
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer ano,
            Model model) {
        Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);

        if (entidade == null) {
            return "redirect:/dashboard";
        }

        // Determina Ano
        int anoSelecionado = ano != null ? ano : java.time.Year.now().getValue();
        int anoAtual = java.time.Year.now().getValue();
        int mesAtual = java.time.LocalDate.now().getMonthValue();

        // Busca todas as importações da entidade (filtro de ano em memória por
        // enquanto)
        // Ideal: Repository.findByEntidadeAndYear
        // Busca todas as importações (para status do mês)
        java.util.List<com.br.sisgetrim.model.doi.DoiImportacao> todasImportacoes = importacaoRepository
                .findByEntidade(entidade);

        // Busca todas as declarações do ano (para lista detalhada)
        java.util.List<com.br.sisgetrim.model.doi.DoiDeclaracao> todasDeclaracoes = doiDeclaracaoRepository
                .findByEntidadeAndAno(entidade, anoSelecionado);

        java.util.List<com.br.sisgetrim.dto.doi.analise.DoiAnaliseMesDTO> analiseMeses = new java.util.ArrayList<>();
        String[] nomesMeses = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto",
                "Setembro", "Outubro", "Novembro", "Dezembro" };

        for (int i = 1; i <= 12; i++) {
            int mesFiltro = i;

            // 1. Determina STATUS do Mês baseado nas IMPORTAÇÕES (Files)
            java.util.List<com.br.sisgetrim.model.doi.DoiImportacao> importsMes = todasImportacoes.stream()
                    .filter(imp -> imp.getCreatedAt() != null &&
                            imp.getCreatedAt().getYear() == anoSelecionado &&
                            imp.getCreatedAt().getMonthValue() == mesFiltro)
                    .toList();

            String statusMes = "FUTURO";
            String statusCor = "text-slate-400";

            boolean possuiConcluido = importsMes.stream()
                    .anyMatch(imp -> "CONCLUIDO".equalsIgnoreCase(imp.getStatus()));

            if (possuiConcluido) {
                statusMes = "Concluído";
                statusCor = "text-emerald-600 bg-emerald-50 border-emerald-100";
            } else {
                if (anoSelecionado < anoAtual) {
                    statusMes = "Atraso";
                    statusCor = "text-rose-600 bg-rose-50 border-rose-100";
                } else if (anoSelecionado == anoAtual) {
                    if (mesFiltro < mesAtual) {
                        statusMes = "Atraso";
                        statusCor = "text-rose-600 bg-rose-50 border-rose-100";
                    } else if (mesFiltro == mesAtual) {
                        statusMes = "Em Aberto";
                        statusCor = "text-amber-600 bg-amber-50 border-amber-100";
                    }
                }
            }

            // 2. Monta LISTA de itens baseado nas DECLARAÇÕES (Records)
            java.util.List<com.br.sisgetrim.dto.doi.analise.DoiDeclaracaoResumoDTO> resumoDeclaracoes = todasDeclaracoes
                    .stream()
                    .filter(dec -> dec.getImportacao() != null &&
                            dec.getImportacao().getCreatedAt().getMonthValue() == mesFiltro) // Filtra pelo mês da
                                                                                             // importação
                    .map(dec -> new com.br.sisgetrim.dto.doi.analise.DoiDeclaracaoResumoDTO(
                            dec.getId(),
                            "IMP-" + dec.getImportacao().getId(), // Protocolo
                            dec.getImportacao().getCreatedAt(), // Data da Inserção (Importação)
                            dec.getMatricula(), // Matrícula
                            dec.getImportacao().getNomeArquivo(),
                            dec.getImportacao().getStatus(), // Status da importação
                            "CONCLUIDO".equalsIgnoreCase(dec.getImportacao().getStatus())
                                    ? "bg-emerald-100 text-emerald-800"
                                    : "ERRO".equalsIgnoreCase(dec.getImportacao().getStatus())
                                            ? "bg-rose-100 text-rose-800"
                                            : "bg-slate-100 text-slate-800"))
                    .sorted((a, b) -> b.dataInsercao().compareTo(a.dataInsercao()))
                    .collect(java.util.stream.Collectors.toList());

            analiseMeses.add(new com.br.sisgetrim.dto.doi.analise.DoiAnaliseMesDTO(
                    nomesMeses[i - 1],
                    i,
                    statusMes,
                    statusCor,
                    resumoDeclaracoes));
        }

        model.addAttribute("analiseMeses", analiseMeses);
        model.addAttribute("anoSelecionado", anoSelecionado);

        // Ano corrente e últimos 5 anos
        java.util.List<Integer> anos = new java.util.ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            anos.add(anoAtual - i);
        }
        model.addAttribute("anosDisponiveis", anos);

        model.addAttribute("entidade", entidade);

        return "malha/analise-importacoes";
    }

    @GetMapping("/doi/declaracao/{id}")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public String visualizarDeclaracao(@PathVariable Long id, Model model) {
        com.br.sisgetrim.model.doi.DoiDeclaracao declaracao = doiDeclaracaoRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Declaração não encontrada"));

        // Força inicialização das coleções lazy
        declaracao.getAlienantes().size();
        declaracao.getAdquirentes().size();

        model.addAttribute("declaracao", declaracao);
        return "malha/doi-detalhes";
    }

    @GetMapping("/doi/{id}/pdf")
    public ResponseEntity<byte[]> exportarPdf(@PathVariable Long id) {
        try {
            DoiDeclaracao declaracao = importacaoDoiService.findByIdWithDetails(id)
                    .orElseThrow(() -> new IllegalArgumentException("Declaração não encontrada: " + id));

            // Força inicialização das coleções lazy para o PDF
            declaracao.getAlienantes().size();
            declaracao.getAdquirentes().size();

            Context context = new Context();
            context.setVariable("declaracao", declaracao);

            String html = templateEngine.process("malha/doi-detalhes-pdf", context);
            byte[] pdfBytes = pdfService.generatePdfFromHtml(html);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "doi-detalhes-" + id + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .header("Content-Type", "text/plain; charset=UTF-8")
                    .body(("Erro ao gerar PDF: " + e.getMessage() + "\nStack Trace:\n"
                            + java.util.Arrays.toString(e.getStackTrace())).getBytes());
        }
    }

    @GetMapping("/importar")
    public String importarPage(@AuthenticationPrincipal Usuario usuarioLogado,
            jakarta.servlet.http.HttpSession session, Model model) {
        Usuario usuario = usuarioService.buscarPorDocumento(usuarioLogado.getDocumento());
        Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);

        if (entidade != null) {
            model.addAttribute("importacoes", importacaoRepository.findByEntidade(entidade));
            model.addAttribute("importacoesItbi",
                    itbiImportacaoRepository.findByEntidadeOrderByCreatedAtDesc(entidade));
            model.addAttribute("entidade", entidade);

            // Lista de Cartórios para o dropdown (apenas se não for usuário de cartório ou
            // se for admin)
            // Lógica: Se o usuário já tem cartório vinculado, ele não escolhe.
            com.br.sisgetrim.model.Cartorio cartorioVinculado = usuario.getCartorios().stream().findFirst()
                    .orElse(null);
            model.addAttribute("cartorioVinculado", cartorioVinculado);

            if (cartorioVinculado == null) {
                model.addAttribute("listaCartorios", cartorioRepository.findByEntidade(entidade));
            }
        }

        return "malha/importar";
    }

    @GetMapping("/manual/importacao-arrecadacao")
    public ResponseEntity<org.springframework.core.io.Resource> baixarManualArrecadacao() {
        try {
            java.io.File file = new java.io.File("C:\\sisgetrim\\Manual Imp Fiscal.pdf");
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(file.toURI());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
