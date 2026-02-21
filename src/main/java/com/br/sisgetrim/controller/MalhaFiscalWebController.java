package com.br.sisgetrim.controller;

import com.br.sisgetrim.repository.doi.DoiImportacaoRepository;
import com.br.sisgetrim.model.Usuario;
import com.br.sisgetrim.model.Entidade;
import com.br.sisgetrim.service.UsuarioService;
import com.br.sisgetrim.repository.FiscalItbiImportacaoRepository;
import com.br.sisgetrim.repository.FiscalItbiRepository;
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
import com.br.sisgetrim.service.ibge.IbgeDeclaracaoService;
import com.br.sisgetrim.service.FiscalItbiService;
import com.br.sisgetrim.service.doi.DoiService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Controller
@RequestMapping("/malha")
public class MalhaFiscalWebController {

        private final DoiImportacaoRepository importacaoRepository;
        private final FiscalItbiImportacaoRepository itbiImportacaoRepository;
        private final com.br.sisgetrim.repository.ibge.IbgeImportacaoRepository ibgeImportacaoRepository;
        private final com.br.sisgetrim.repository.ibge.IbgeDeclaracaoRepository ibgeDeclaracaoRepository;
        private final UsuarioService usuarioService;
        private final com.br.sisgetrim.repository.CartorioRepository cartorioRepository;
        private final com.br.sisgetrim.repository.doi.DoiDeclaracaoRepository doiDeclaracaoRepository;
        private final ImportacaoDoiService importacaoDoiService;
        private final PdfService pdfService;
        private final TemplateEngine templateEngine;
        private final FiscalItbiRepository fiscalItbiRepository;
        private final IbgeDeclaracaoService ibgeDeclaracaoService;
        private final FiscalItbiService fiscalItbiService;
        private final DoiService doiService;

        @Autowired
        public MalhaFiscalWebController(DoiImportacaoRepository importacaoRepository,
                        FiscalItbiImportacaoRepository itbiImportacaoRepository,
                        com.br.sisgetrim.repository.ibge.IbgeImportacaoRepository ibgeImportacaoRepository,
                        com.br.sisgetrim.repository.ibge.IbgeDeclaracaoRepository ibgeDeclaracaoRepository,
                        UsuarioService usuarioService,
                        com.br.sisgetrim.repository.CartorioRepository cartorioRepository,
                        com.br.sisgetrim.repository.doi.DoiDeclaracaoRepository doiDeclaracaoRepository,
                        ImportacaoDoiService importacaoDoiService,
                        PdfService pdfService,
                        TemplateEngine templateEngine,
                        FiscalItbiRepository fiscalItbiRepository,
                        IbgeDeclaracaoService ibgeDeclaracaoService,
                        FiscalItbiService fiscalItbiService,
                        DoiService doiService) {
                this.importacaoRepository = importacaoRepository;
                this.itbiImportacaoRepository = itbiImportacaoRepository;
                this.ibgeImportacaoRepository = ibgeImportacaoRepository;
                this.ibgeDeclaracaoRepository = ibgeDeclaracaoRepository;
                this.usuarioService = usuarioService;
                this.cartorioRepository = cartorioRepository;
                this.doiDeclaracaoRepository = doiDeclaracaoRepository;
                this.importacaoDoiService = importacaoDoiService;
                this.pdfService = pdfService;
                this.templateEngine = templateEngine;
                this.fiscalItbiRepository = fiscalItbiRepository;
                this.ibgeDeclaracaoService = ibgeDeclaracaoService;
                this.fiscalItbiService = fiscalItbiService;
                this.doiService = doiService;
        }

        @GetMapping("/analise-importacoes")
        public String analiseImportacoes(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer ano,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer mes,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "dataLavratura") String sort,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "desc") String dir,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Long cartorioId,
                        Model model) {
                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);

                if (entidade == null) {
                        return "redirect:/dashboard";
                }

                // Determina Ano e Mês
                int anoAtual = java.time.Year.now().getValue();
                int anoSelecionado = (ano != null) ? ano : anoAtual;
                int mesSelecionado = (mes != null) ? mes : 0; // 0 = Todos por padrão

                // Calcula intervalo de datas para o filtro
                java.time.LocalDate inicio;
                java.time.LocalDate fim;

                if (mesSelecionado == 0) {
                        inicio = java.time.LocalDate.of(anoSelecionado, 1, 1);
                        fim = java.time.LocalDate.of(anoSelecionado, 12, 31);
                } else {
                        inicio = java.time.LocalDate.of(anoSelecionado, mesSelecionado, 1);
                        fim = inicio.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());
                }

                // Configura ordenação dinâmica
                org.springframework.data.domain.Sort sorting = dir.equalsIgnoreCase("asc")
                                ? org.springframework.data.domain.Sort.by(sort).ascending()
                                : org.springframework.data.domain.Sort.by(sort).descending();

                // Busca declarações filtradas diretamente no banco de dados por período e
                // ordenação
                java.util.List<com.br.sisgetrim.model.doi.DoiDeclaracao> todasDeclaracoes = doiDeclaracaoRepository
                                .findByEntidadeAndPeriodo(entidade, inicio, fim, cartorioId, sorting);

                java.time.LocalDate minData = doiDeclaracaoRepository.findMinDataLavratura(entidade);
                java.time.LocalDate maxData = doiDeclaracaoRepository.findMaxDataLavratura(entidade);

                model.addAttribute("sortAtual", sort);
                model.addAttribute("dirAtual", dir.toLowerCase());
                model.addAttribute("cartorioSelecionado", cartorioId);
                model.addAttribute("cartoriosDisponiveis", cartorioRepository.findByEntidade(entidade));

                System.out.println(">>> DEBUG DOI: Encontrados " + todasDeclaracoes.size() + " registros no período "
                                + inicio
                                + " a " + fim);
                System.out.println(">>> DEBUG DOI: Intervalo total no DB para esta entidade: " + minData + " até "
                                + maxData);

                java.util.List<com.br.sisgetrim.dto.doi.analise.DoiDeclaracaoResumoDTO> listaDeclaracoes = todasDeclaracoes
                                .stream()
                                .map(dec -> new com.br.sisgetrim.dto.doi.analise.DoiDeclaracaoResumoDTO(
                                                dec.getId(),
                                                "IMP-" + dec.getImportacao().getId(), // Protocolo
                                                dec.getImportacao().getCreatedAt(), // Data da Inserção
                                                dec.getDataLavratura(), // Data Lavratura
                                                dec.getMatricula(),
                                                dec.getImportacao().getCartorio() != null
                                                                ? dec.getImportacao().getCartorio().getDenominacao()
                                                                : "Não informado",
                                                dec.getOperacao() != null
                                                                ? dec.getOperacao().getValorOperacaoImobiliaria()
                                                                : null,
                                                dec.getImportacao().getStatus(),
                                                "CONCLUIDO".equalsIgnoreCase(dec.getImportacao().getStatus())
                                                                ? "bg-emerald-100 text-emerald-800"
                                                                : "ERRO".equalsIgnoreCase(
                                                                                dec.getImportacao().getStatus())
                                                                                                ? "bg-rose-100 text-rose-800"
                                                                                                : "bg-slate-100 text-slate-800"))
                                .collect(java.util.stream.Collectors.toList());

                model.addAttribute("listaDeclaracoes", listaDeclaracoes);
                model.addAttribute("anoSelecionado", anoSelecionado);
                model.addAttribute("mesSelecionado", mesSelecionado);

                // Ano corrente e últimos 5 anos
                java.util.List<Integer> anosDisponiveis = new java.util.ArrayList<>();
                for (int i = 0; i < 6; i++) {
                        anosDisponiveis.add(anoAtual - i);
                }
                model.addAttribute("anosDisponiveis", anosDisponiveis);

                model.addAttribute("entidade", entidade);

                return "malha/analise-importacoes";
        }

        @GetMapping("/analise-fiscal")
        public String analiseFiscal(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer ano,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer mes,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "id") String sort,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "desc") String dir,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Long cartorioId,
                        Model model) {

                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return "redirect:/dashboard";

                // Filtros de Ano (De 2020 até o ano atual + 1)
                int anoAtual = java.time.LocalDate.now().getYear();
                java.util.List<Integer> anosDisponiveis = new java.util.ArrayList<>();
                for (int i = 0; i < 6; i++) {
                        anosDisponiveis.add(anoAtual - i);
                }

                int anoSelecionado = (ano != null) ? ano : anoAtual;
                int mesSelecionado = (mes != null) ? mes : 0;

                // Atributos para o template
                model.addAttribute("anosDisponiveis", anosDisponiveis);
                model.addAttribute("anoSelecionado", anoSelecionado);
                model.addAttribute("mesSelecionado", mesSelecionado);
                model.addAttribute("sortAtual", sort);
                model.addAttribute("dirAtual", dir.toLowerCase());
                model.addAttribute("cartorioSelecionado", cartorioId);
                model.addAttribute("cartoriosDisponiveis", cartorioRepository.findByEntidade(entidade));

                // Cáculo de datas para o período
                java.time.LocalDate inicio = java.time.LocalDate.of(anoSelecionado,
                                (mesSelecionado == 0 ? 1 : mesSelecionado), 1);
                java.time.LocalDate fim = (mesSelecionado == 0)
                                ? java.time.LocalDate.of(anoSelecionado, 12, 31)
                                : inicio.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

                // Busca real com ordenação
                org.springframework.data.domain.Sort sorting = dir.equalsIgnoreCase("asc")
                                ? org.springframework.data.domain.Sort.by(sort).ascending()
                                : org.springframework.data.domain.Sort.by(sort).descending();

                java.util.List<com.br.sisgetrim.model.FiscalItbi> listaItbi = fiscalItbiRepository
                                .findByEntidadeAndPeriodo(entidade, inicio, fim, sorting);

                // Mapeamento para DTO
                java.util.List<com.br.sisgetrim.dto.fiscal.analise.FiscalItbiResumoDTO> listaDTO = listaItbi.stream()
                                .map(itbi -> new com.br.sisgetrim.dto.fiscal.analise.FiscalItbiResumoDTO(
                                                itbi.getId(),
                                                itbi.getItbiNumero() + "/" + itbi.getItbiAno(),
                                                itbi.getItbiData(),
                                                itbi.getItbiProprietarioNome(),
                                                itbi.getItbiAdquirenteNome(),
                                                itbi.getItbiValorVenalCalculadoTotal(),
                                                itbi.getItbiSituacao(),
                                                itbi.getItbiSituacao() != null
                                                                && itbi.getItbiSituacao().equalsIgnoreCase("PAGO")
                                                                                ? "bg-emerald-500/10 text-emerald-600 border-emerald-500/20"
                                                                                : "bg-slate-500/10 text-slate-600 border-slate-500/20"))
                                .toList();

                model.addAttribute("declaracoes", listaDTO);

                // Totais para a tela
                java.math.BigDecimal totalGeral = listaDTO.stream()
                                .map(com.br.sisgetrim.dto.fiscal.analise.FiscalItbiResumoDTO::valorVenalTotal)
                                .filter(java.util.Objects::nonNull)
                                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

                model.addAttribute("totalRegistros", listaDTO.size());
                model.addAttribute("totalGeral", totalGeral);

                return "malha/analise-fiscal";
        }

        @GetMapping("/analise-ibge")
        public String analiseIbge(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer ano,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer mes,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "id") String sort,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "desc") String dir,
                        Model model) {

                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return "redirect:/dashboard";

                // Filtros de Ano
                int anoAtual = java.time.LocalDate.now().getYear();
                java.util.List<Integer> anosDisponiveis = new java.util.ArrayList<>();
                for (int i = 0; i < 6; i++) {
                        anosDisponiveis.add(anoAtual - i);
                }

                int anoSelecionado = (ano != null) ? ano : anoAtual;
                int mesSelecionado = (mes != null) ? mes : 0;

                // Atributos para o template
                model.addAttribute("anosDisponiveis", anosDisponiveis);
                model.addAttribute("anoSelecionado", anoSelecionado);
                model.addAttribute("mesSelecionado", mesSelecionado);
                model.addAttribute("sortAtual", sort);
                model.addAttribute("dirAtual", dir.toLowerCase());

                // Cálculo do período
                java.time.LocalDate inicio = java.time.LocalDate.of(anoSelecionado,
                                (mesSelecionado == 0 ? 1 : mesSelecionado), 1);
                java.time.LocalDate fim = (mesSelecionado == 0)
                                ? java.time.LocalDate.of(anoSelecionado, 12, 31)
                                : inicio.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

                // Ordenação dinâmica
                org.springframework.data.domain.Sort sorting = dir.equalsIgnoreCase("asc")
                                ? org.springframework.data.domain.Sort.by(sort).ascending()
                                : org.springframework.data.domain.Sort.by(sort).descending();

                java.util.List<com.br.sisgetrim.model.ibge.IbgeDeclaracao> listaIbge = ibgeDeclaracaoRepository
                                .findByEntidadeAndPeriodo(entidade, inicio, fim, sorting);

                // Mapeamento para DTO
                java.util.List<com.br.sisgetrim.dto.ibge.analise.IbgeDeclaracaoResumoDTO> listaDTO = listaIbge.stream()
                                .map(ibge -> {
                                        String adquirenteNome = (ibge.getAdquirentes() != null
                                                        && !ibge.getAdquirentes().isEmpty())
                                                                        ? buscarNomeCpfCnpj(ibge.getAdquirentes().get(0)
                                                                                        .getNi())
                                                                        : "N/A";

                                        String alienanteNome = (ibge.getAlienantes() != null
                                                        && !ibge.getAlienantes().isEmpty())
                                                                        ? buscarNomeCpfCnpj(ibge.getAlienantes().get(0)
                                                                                        .getNi())
                                                                        : "N/A";

                                        String classStatus = "bg-slate-500/10 text-slate-600 border-slate-500/20";
                                        if (ibge.getIbgeImportacao() != null && "CONCLUIDO".equalsIgnoreCase(
                                                        ibge.getIbgeImportacao().getStatus())) {
                                                classStatus = "bg-indigo-500/10 text-indigo-600 border-indigo-500/20";
                                        }

                                        return new com.br.sisgetrim.dto.ibge.analise.IbgeDeclaracaoResumoDTO(
                                                        ibge.getId(),
                                                        "IMP-" + (ibge.getIbgeImportacao() != null
                                                                        ? ibge.getIbgeImportacao().getId()
                                                                        : "N/A"),
                                                        ibge.getDataLavraturaRegistroAverbacao(),
                                                        ibge.getNomeCartorio(),
                                                        alienanteNome,
                                                        adquirenteNome,
                                                        ibge.getIbgeImportacao() != null
                                                                        ? ibge.getIbgeImportacao().getStatus()
                                                                        : "N/A",
                                                        classStatus);
                                })
                                .toList();

                model.addAttribute("declaracoes", listaDTO);
                model.addAttribute("totalRegistros", listaDTO.size());

                return "malha/analise-ibge";
        }

        private String formatarCpfCnpj(String documento) {
                if (documento == null)
                        return "N/A";
                String doc = documento.replaceAll("[^0-9]", "");
                if (doc.length() == 11) {
                        return doc.substring(0, 3) + "." + doc.substring(3, 6) + "." + doc.substring(6, 9) + "-"
                                        + doc.substring(9);
                } else if (doc.length() == 14) {
                        return doc.substring(0, 2) + "." + doc.substring(2, 5) + "." + doc.substring(5, 8) + "/"
                                        + doc.substring(8, 12) + "-" + doc.substring(12);
                }
                return documento;
        }

        private String buscarNomeCpfCnpj(String documento) {
                if (documento == null || documento.isBlank())
                        return "N/A";
                String limpo = documento.replaceAll("[^0-9]", "");
                String formatado = formatarCpfCnpj(limpo);

                try {
                        com.br.sisgetrim.model.FiscalItbi t = fiscalItbiRepository
                                        .findFirstByItbiTransmitenteCpfOrderByIdDesc(limpo);
                        if (t != null && t.getItbiTransmitenteNome() != null && !t.getItbiTransmitenteNome().isBlank())
                                return t.getItbiTransmitenteNome() + " (" + formatado + ")";

                        com.br.sisgetrim.model.FiscalItbi a = fiscalItbiRepository
                                        .findFirstByItbiAdquirenteCpfOrderByIdDesc(limpo);
                        if (a != null && a.getItbiAdquirenteNome() != null && !a.getItbiAdquirenteNome().isBlank())
                                return a.getItbiAdquirenteNome() + " (" + formatado + ")";

                        com.br.sisgetrim.model.FiscalItbi p = fiscalItbiRepository
                                        .findFirstByItbiProprietarioCpfOrderByIdDesc(limpo);
                        if (p != null && p.getItbiProprietarioNome() != null && !p.getItbiProprietarioNome().isBlank())
                                return p.getItbiProprietarioNome() + " (" + formatado + ")";
                } catch (Exception e) {
                        // ignore fallback errors
                }

                return formatado;
        }

        @GetMapping("/analise-importacoes/pdf")
        public ResponseEntity<byte[]> exportarPdfRelatorio(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer ano,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer mes,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "dataLavratura") String sort,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "desc") String dir,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Long cartorioId) {

                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return ResponseEntity.badRequest().build();

                // Lógica de Filtros (Igual à tela)
                int anoAtual = java.time.Year.now().getValue();
                int anoSelecionado = (ano != null) ? ano : anoAtual;
                int mesSelecionado = (mes != null) ? mes : 0;

                java.time.LocalDate inicio = (mesSelecionado == 0)
                                ? java.time.LocalDate.of(anoSelecionado, 1, 1)
                                : java.time.LocalDate.of(anoSelecionado, mesSelecionado, 1);
                java.time.LocalDate fim = (mesSelecionado == 0)
                                ? java.time.LocalDate.of(anoSelecionado, 12, 31)
                                : inicio.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

                org.springframework.data.domain.Sort sorting = dir.equalsIgnoreCase("asc")
                                ? org.springframework.data.domain.Sort.by(sort).ascending()
                                : org.springframework.data.domain.Sort.by(sort).descending();

                java.util.List<com.br.sisgetrim.model.doi.DoiDeclaracao> todasDeclaracoes = doiDeclaracaoRepository
                                .findByEntidadeAndPeriodo(entidade, inicio, fim, cartorioId, sorting);

                java.util.List<com.br.sisgetrim.dto.doi.analise.DoiDeclaracaoResumoDTO> listaDTO = todasDeclaracoes
                                .stream()
                                .map(dec -> new com.br.sisgetrim.dto.doi.analise.DoiDeclaracaoResumoDTO(
                                                dec.getId(), dec.getImportacao().getId().toString(),
                                                dec.getDataCadastro(),
                                                dec.getDataLavratura(), dec.getMatricula(),
                                                dec.getImportacao().getCartorio() != null
                                                                ? dec.getImportacao().getCartorio().getDenominacao()
                                                                : "Não informado",
                                                dec.getOperacao() != null
                                                                ? dec.getOperacao().getValorOperacaoImobiliaria()
                                                                : null,
                                                dec.getImportacao().getStatus(), ""))
                                .toList();

                // Cálculos de Totais
                java.math.BigDecimal totalGeral = listaDTO.stream()
                                .map(com.br.sisgetrim.dto.doi.analise.DoiDeclaracaoResumoDTO::valorOperacao)
                                .filter(java.util.Objects::nonNull)
                                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

                // Preparar Contexto Thymeleaf
                Context context = new Context();
                context.setVariable("listaDeclaracoes", listaDTO);
                context.setVariable("totalGeral", totalGeral);
                context.setVariable("totalRegistros", listaDTO.size());
                context.setVariable("periodoExtenso",
                                (mesSelecionado == 0 ? "ANO " : getMesNome(mesSelecionado) + " / ") + anoSelecionado);
                context.setVariable("cartorioNome",
                                cartorioId != null
                                                ? cartorioRepository.findById(cartorioId).map(c -> c.getDenominacao())
                                                                .orElse(null)
                                                : null);

                String htmlContent = templateEngine.process("malha/analise-importacoes-pdf", context);

                try {
                        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);
                        String filename = "relatorio-doi-" + anoSelecionado
                                        + (mesSelecionado > 0 ? "-" + mesSelecionado : "")
                                        + ".pdf";

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                                        .contentType(MediaType.APPLICATION_PDF)
                                        .body(pdfBytes);
                } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                }
        }

        @GetMapping("/analise-fiscal/pdf")
        public ResponseEntity<byte[]> exportarPdfRelatorioFiscal(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer ano,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer mes,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "id") String sort,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "desc") String dir) {

                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return ResponseEntity.badRequest().build();

                // Lógica de Filtros
                int anoAtual = java.time.LocalDate.now().getYear();
                int anoSelecionado = (ano != null) ? ano : anoAtual;
                int mesSelecionado = (mes != null) ? mes : 0;

                java.time.LocalDate inicio = java.time.LocalDate.of(anoSelecionado,
                                (mesSelecionado == 0 ? 1 : mesSelecionado), 1);
                java.time.LocalDate fim = (mesSelecionado == 0)
                                ? java.time.LocalDate.of(anoSelecionado, 12, 31)
                                : inicio.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

                // Busca real com ordenação
                org.springframework.data.domain.Sort sorting = dir.equalsIgnoreCase("asc")
                                ? org.springframework.data.domain.Sort.by(sort).ascending()
                                : org.springframework.data.domain.Sort.by(sort).descending();

                java.util.List<com.br.sisgetrim.model.FiscalItbi> listaItbi = fiscalItbiRepository
                                .findByEntidadeAndPeriodo(entidade, inicio, fim, sorting);

                // Mapeamento para DTO
                java.util.List<com.br.sisgetrim.dto.fiscal.analise.FiscalItbiResumoDTO> listaDTO = listaItbi.stream()
                                .map(itbi -> new com.br.sisgetrim.dto.fiscal.analise.FiscalItbiResumoDTO(
                                                itbi.getId(),
                                                itbi.getItbiNumero() + "/" + itbi.getItbiAno(),
                                                itbi.getItbiData(),
                                                itbi.getItbiProprietarioNome(),
                                                itbi.getItbiAdquirenteNome(),
                                                itbi.getItbiValorVenalCalculadoTotal(),
                                                itbi.getItbiSituacao(),
                                                itbi.getItbiSituacao() != null
                                                                && itbi.getItbiSituacao().equalsIgnoreCase("PAGO")
                                                                                ? "PAGO"
                                                                                : "PENDENTE"))
                                .toList();

                // Cálculos de Totais
                java.math.BigDecimal totalGeral = listaDTO.stream()
                                .map(com.br.sisgetrim.dto.fiscal.analise.FiscalItbiResumoDTO::valorVenalTotal)
                                .filter(java.util.Objects::nonNull)
                                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

                // Preparar Contexto Thymeleaf
                Context context = new Context();
                context.setVariable("listaDeclaracoes", listaDTO);
                context.setVariable("totalGeral", totalGeral);
                context.setVariable("totalRegistros", listaDTO.size());
                context.setVariable("periodoExtenso",
                                (mesSelecionado == 0 ? "TODOS OS MESES / " : getMesNome(mesSelecionado) + " / ")
                                                + anoSelecionado);

                String htmlContent = templateEngine.process("malha/analise-fiscal-pdf", context);

                try {
                        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);
                        String filename = "relatorio-fiscal-itbi-" + anoSelecionado
                                        + (mesSelecionado > 0 ? "-" + mesSelecionado : "")
                                        + ".pdf";

                        return ResponseEntity.ok()
                                        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                                                        "attachment; filename=" + filename)
                                        .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                                        .body(pdfBytes);
                } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                }
        }

        private String getMesNome(int mes) {
                String[] meses = { "", "JANEIRO", "FEVEREIRO", "MARÇO", "ABRIL", "MAIO", "JUNHO", "JULHO", "AGOSTO",
                                "SETEMBRO",
                                "OUTUBRO", "NOVEMBRO", "DEZEMBRO" };
                return (mes >= 1 && mes <= 12) ? meses[mes] : "";
        }

        @GetMapping("/analise-fiscal/{id}")
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public String visualizarFiscal(@PathVariable Long id,
                        @AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        Model model) {
                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return "redirect:/dashboard";

                com.br.sisgetrim.model.FiscalItbi itbi = fiscalItbiRepository.findById(id)
                                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.NOT_FOUND,
                                                "Registro fiscal não encontrado"));

                // Segurança: Verificar se pertence à mesma entidade
                if (!itbi.getEntidade().getId().equals(entidade.getId())) {
                        throw new org.springframework.web.server.ResponseStatusException(
                                        org.springframework.http.HttpStatus.FORBIDDEN,
                                        "Acesso negado");
                }

                model.addAttribute("itbi", itbi);
                return "malha/analise-fiscal-detalhes";
        }

        @org.springframework.web.bind.annotation.PostMapping("/analise-fiscal/{id}/salvar")
        public String salvarFiscal(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @PathVariable Long id,
                        @org.springframework.web.bind.annotation.ModelAttribute("itbi") com.br.sisgetrim.model.FiscalItbi itbiForm,
                        org.springframework.validation.BindingResult bindingResult,
                        org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return "redirect:/dashboard";

                if (bindingResult.hasErrors()) {
                        System.err.println("ERROS DE BINDING DETECTADOS:");
                        bindingResult.getAllErrors().forEach(error -> {
                                System.err.println("Erro: " + error.toString());
                        });
                }

                try {
                        System.out.println("Salvando Fiscal ID: " + id);
                        System.out.println("Data recebida: " + itbiForm.getItbiData());
                        System.out.println("Numero recebido: " + itbiForm.getItbiNumero());

                        fiscalItbiService.atualizarFiscal(id, itbiForm);
                        redirectAttributes.addFlashAttribute("mensagemSucesso", "Alterações salvas com sucesso!");
                } catch (Exception e) {
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("mensagemErro",
                                        "Erro ao salvar alterações: " + e.getMessage());
                        return "redirect:/malha/analise-fiscal/" + id;
                }

                return "redirect:/malha/analise-fiscal/" + id;
        }

        @GetMapping("/doi/declaracao/{id}")
        @org.springframework.transaction.annotation.Transactional(readOnly = true)
        public String visualizarDeclaracao(@PathVariable Long id,
                        @AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        Model model) {
                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return "redirect:/dashboard";
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

        @GetMapping("/analise-fiscal/{id}/pdf")
        public ResponseEntity<byte[]> exportarPdfFiscal(@PathVariable Long id,
                        @AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session) {
                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();

                com.br.sisgetrim.model.FiscalItbi itbi = fiscalItbiRepository.findById(id)
                                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.NOT_FOUND,
                                                "Registro fiscal não encontrado"));

                if (!itbi.getEntidade().getId().equals(entidade.getId())) {
                        return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).build();
                }

                org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
                context.setVariable("itbi", itbi);

                try {
                        String htmlContent = templateEngine.process("malha/analise-fiscal-detalhes-pdf", context);
                        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);
                        String filename = "detalhes-fiscal-" + itbi.getItbiNumero() + "-" + itbi.getItbiAno() + ".pdf";

                        return ResponseEntity.ok()
                                        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                                                        "attachment; filename=" + filename)
                                        .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                                        .body(pdfBytes);
                } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                }
        }

        @GetMapping("/doi/{id}/pdf")
        public ResponseEntity<byte[]> exportarPdf(@PathVariable Long id) {
                try {
                        DoiDeclaracao declaracao = importacaoDoiService.findByIdWithDetails(id)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                        "Declaração não encontrada: " + id));

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
                        model.addAttribute("importacoesIbge",
                                        ibgeImportacaoRepository.findByEntidadeOrderByCreatedAtDesc(entidade));
                        model.addAttribute("entidade", entidade);

                        // Lista de Cartórios para o dropdown
                        java.util.Set<com.br.sisgetrim.model.Cartorio> userCartorios = usuario.getCartorios();
                        if (userCartorios != null && !userCartorios.isEmpty()) {
                                java.util.List<com.br.sisgetrim.model.Cartorio> filteredCartorios = userCartorios
                                                .stream()
                                                .filter(c -> c.getEntidade() != null
                                                                && c.getEntidade().getId().equals(entidade.getId()))
                                                .collect(java.util.stream.Collectors.toList());
                                model.addAttribute("listaCartorios", filteredCartorios);
                        } else {
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

                        org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(
                                        file.toURI());
                        return ResponseEntity.ok()
                                        .contentType(MediaType.APPLICATION_PDF)
                                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                                        "inline; filename=\"" + file.getName() + "\"")
                                        .body(resource);
                } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                }
        }

        @GetMapping("/analise-ibge/pdf")
        public ResponseEntity<byte[]> exportarPdfIbge(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer ano,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) Integer mes,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "dataLavraturaRegistroAverbacao") String sort,
                        @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "desc") String dir) {

                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return ResponseEntity.badRequest().build();

                int anoAtual = java.time.Year.now().getValue();
                int anoSelecionado = (ano != null) ? ano : anoAtual;
                int mesSelecionado = (mes != null) ? mes : 0;

                java.time.LocalDate inicio = (mesSelecionado == 0)
                                ? java.time.LocalDate.of(anoSelecionado, 1, 1)
                                : java.time.LocalDate.of(anoSelecionado, mesSelecionado, 1);
                java.time.LocalDate fim = (mesSelecionado == 0)
                                ? java.time.LocalDate.of(anoSelecionado, 12, 31)
                                : inicio.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());

                org.springframework.data.domain.Sort sorting = dir.equalsIgnoreCase("asc")
                                ? org.springframework.data.domain.Sort.by(sort).ascending()
                                : org.springframework.data.domain.Sort.by(sort).descending();

                java.util.List<com.br.sisgetrim.model.ibge.IbgeDeclaracao> listaIbge = ibgeDeclaracaoRepository
                                .findByEntidadeAndPeriodo(entidade, inicio, fim, sorting);

                java.util.List<com.br.sisgetrim.dto.ibge.analise.IbgeDeclaracaoResumoDTO> listaDTO = listaIbge.stream()
                                .map(ibge -> {
                                        String adquirenteNome = (ibge.getAdquirentes() != null
                                                        && !ibge.getAdquirentes().isEmpty())
                                                                        ? buscarNomeCpfCnpj(ibge.getAdquirentes().get(0)
                                                                                        .getNi())
                                                                        : "N/A";

                                        String alienanteNome = (ibge.getAlienantes() != null
                                                        && !ibge.getAlienantes().isEmpty())
                                                                        ? buscarNomeCpfCnpj(ibge.getAlienantes().get(0)
                                                                                        .getNi())
                                                                        : "N/A";

                                        String classStatus = "bg-slate-500/10 text-slate-600 border-slate-500/20";
                                        String status = "N/A";
                                        String protocolo = "N/A";

                                        if (ibge.getIbgeImportacao() != null) {
                                                status = ibge.getIbgeImportacao().getStatus();
                                                protocolo = "IMP-" + ibge.getIbgeImportacao().getId();
                                                if ("CONCLUIDO".equalsIgnoreCase(status)) {
                                                        classStatus = "bg-indigo-500/10 text-indigo-600 border-indigo-500/20";
                                                }
                                        }

                                        return new com.br.sisgetrim.dto.ibge.analise.IbgeDeclaracaoResumoDTO(
                                                        ibge.getId(),
                                                        protocolo,
                                                        ibge.getDataLavraturaRegistroAverbacao(),
                                                        ibge.getNomeCartorio(),
                                                        alienanteNome,
                                                        adquirenteNome,
                                                        status,
                                                        classStatus);
                                })
                                .toList();

                Context context = new Context();
                context.setVariable("listaDeclaracoes", listaDTO);
                context.setVariable("totalRegistros", listaDTO.size());
                context.setVariable("periodoExtenso",
                                (mesSelecionado == 0 ? "ANO " : getMesNome(mesSelecionado) + " / ") + anoSelecionado);

                String htmlContent = templateEngine.process("malha/analise-ibge-pdf", context);

                try {
                        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);
                        String filename = "relatorio-ibge-" + anoSelecionado
                                        + (mesSelecionado > 0 ? "-" + mesSelecionado : "")
                                        + ".pdf";

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                                        .contentType(MediaType.APPLICATION_PDF)
                                        .body(pdfBytes);
                } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                }
        }

        @GetMapping("/analise-ibge/{id}/pdf")
        public ResponseEntity<byte[]> exportarPdfIbgeDetalhes(@PathVariable Long id,
                        @AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session) {
                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();

                com.br.sisgetrim.model.ibge.IbgeDeclaracao ibge = ibgeDeclaracaoRepository.findById(id)
                                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                                                org.springframework.http.HttpStatus.NOT_FOUND,
                                                "Registro IBGE não encontrado"));

                if (!ibge.getEntidade().getId().equals(entidade.getId())) {
                        return ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).build();
                }

                Context context = new Context();
                context.setVariable("ibge", ibge);

                // Nomes cruzados para o PDF
                java.util.Map<Long, String> nomeAlienanteMap = new java.util.HashMap<>();
                ibge.getAlienantes().forEach(a -> nomeAlienanteMap.put(a.getId(), buscarNomeCpfCnpj(a.getNi())));

                java.util.Map<Long, String> nomeAdquirenteMap = new java.util.HashMap<>();
                ibge.getAdquirentes().forEach(a -> nomeAdquirenteMap.put(a.getId(), buscarNomeCpfCnpj(a.getNi())));

                context.setVariable("nomeAlienanteMap", nomeAlienanteMap);
                context.setVariable("nomeAdquirenteMap", nomeAdquirenteMap);

                try {
                        String htmlContent = templateEngine.process("malha/analise-ibge-detalhes-pdf", context);
                        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);
                        String filename = "detalhes-ibge-" + id + ".pdf";

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                                        .contentType(MediaType.APPLICATION_PDF)
                                        .body(pdfBytes);
                } catch (Exception e) {
                        return ResponseEntity.internalServerError().build();
                }
        }

        @GetMapping("/analise-ibge/{id}")
        public String analiseIbgeDetalhes(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @PathVariable Long id,
                        Model model) {
                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return "redirect:/dashboard";

                com.br.sisgetrim.model.ibge.IbgeDeclaracao declaracao = ibgeDeclaracaoRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Declaração não encontrada: " + id));

                // Mapeia nomes cruzados para a view
                declaracao.getAlienantes().forEach(a -> {
                        model.addAttribute("nomeAlienante_" + a.getId(), buscarNomeCpfCnpj(a.getNi()));
                });
                declaracao.getAdquirentes().forEach(a -> {
                        model.addAttribute("nomeAdquirente_" + a.getId(), buscarNomeCpfCnpj(a.getNi()));
                });

                model.addAttribute("ibge", declaracao);
                return "malha/analise-ibge-detalhes";
        }

        @org.springframework.web.bind.annotation.PostMapping("/analise-ibge/{id}/salvar")
        public String salvarIbgeDetalhes(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @PathVariable Long id,
                        @org.springframework.web.bind.annotation.ModelAttribute("ibge") com.br.sisgetrim.model.ibge.IbgeDeclaracao ibgeForm,
                        org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null)
                        return "redirect:/dashboard";

                try {
                        ibgeDeclaracaoService.atualizarDeclaracao(id, ibgeForm);
                        redirectAttributes.addFlashAttribute("mensagemSucesso", "Alterações salvas com sucesso!");
                } catch (Exception e) {
                        redirectAttributes.addFlashAttribute("mensagemErro",
                                        "Erro ao salvar alterações: " + e.getMessage());
                        return "redirect:/malha/analise-ibge/" + id;
                }

                return "redirect:/malha/analise-ibge/" + id;
        }

        @org.springframework.web.bind.annotation.PostMapping("/doi/{id}/salvar")
        public String salvarDoiDetalhes(@AuthenticationPrincipal Usuario usuarioLogado,
                        jakarta.servlet.http.HttpSession session,
                        @PathVariable Long id,
                        @org.springframework.web.bind.annotation.ModelAttribute("declaracao") com.br.sisgetrim.model.doi.DoiDeclaracao doiForm,
                        org.springframework.validation.BindingResult bindingResult,
                        org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

                if (bindingResult.hasErrors()) {
                        System.err.println(">>> DEBUG DOI: Erros de Binding no salvamento:");
                        bindingResult.getAllErrors().forEach(error -> System.err.println("  - " + error.toString()));
                }

                Entidade entidade = usuarioService.getEntidadeSelecionada(usuarioLogado, session);
                if (entidade == null) {
                        System.err.println(">>> DEBUG DOI: Entidade nula no POST /doi/" + id + "/salvar");
                        System.err.println(">>> DEBUG DOI: Usuario Logado: "
                                        + (usuarioLogado != null ? usuarioLogado.getDocumento() : "NULL"));
                        return "redirect:/dashboard";
                }

                try {
                        System.out.println("Salvando DOI ID: " + id);
                        doiService.atualizarDeclaracao(id, doiForm);
                        redirectAttributes.addFlashAttribute("mensagemSucesso", "Alterações salvas com sucesso!");
                } catch (Exception e) {
                        System.err.println("Erro ao salvar DOI: " + e.getMessage());
                        e.printStackTrace();
                        redirectAttributes.addFlashAttribute("mensagemErro",
                                        "Erro ao salvar alterações: " + e.getMessage());
                        return "redirect:/malha/doi/declaracao/" + id;
                }

                return "redirect:/malha/doi/declaracao/" + id;
        }
}
