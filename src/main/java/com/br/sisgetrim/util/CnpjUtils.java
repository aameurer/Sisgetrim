package com.br.sisgetrim.util;

public class CnpjUtils {

    public static String format(String cnpj) {
        if (cnpj == null)
            return "";
        String clean = cnpj.replaceAll("\\D", "");

        // Se tiver menos de 14, preenche com zeros à esquerda (ex: prefeituras)
        while (clean.length() < 14) {
            clean = "0" + clean;
        }

        if (clean.length() > 14)
            return cnpj;

        return String.format("%s.%s.%s/%s-%s",
                clean.substring(0, 2),
                clean.substring(2, 5),
                clean.substring(5, 8),
                clean.substring(8, 12),
                clean.substring(12, 14));
    }
}
