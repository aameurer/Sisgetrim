package com.br.sisgetrim.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
        logger.warn("Erro de validação (IAE): {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        return "redirect:/cadastro";
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException ex, RedirectAttributes redirectAttributes) {
        logger.warn("Erro de estado (ISE): {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        return "redirect:/cadastro";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, RedirectAttributes redirectAttributes) {
        // Log critico para console visível
        System.err.println("=== ERRO GLOBAL CAPTURADO ===");
        ex.printStackTrace();

        logger.error("ERRO GLOBAL NÃO TRATADO: ", ex);

        // Se for erro de acesso (Security), relançar para o Spring Security tratar
        if (ex instanceof org.springframework.security.access.AccessDeniedException) {
            throw (RuntimeException) ex;
        }

        redirectAttributes.addFlashAttribute("erro",
                "Ocorreu um erro interno no servidor: " + ex.getMessage());
        return "redirect:/cartorios/cadastro"; // Redireciona para onde o usuário estava (ou lista)
    }
}
