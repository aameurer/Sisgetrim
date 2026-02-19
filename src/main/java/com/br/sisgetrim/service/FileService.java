package com.br.sisgetrim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private final Path root = Paths.get("uploads");

    public FileService() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
                logger.info("Pasta base de uploads criada em: {}", root.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível inicializar a pasta base de uploads", e);
        }
    }

    public String salvar(MultipartFile arquivo) {
        return salvar(arquivo, "usuarios");
    }

    public String salvar(MultipartFile arquivo, String subpasta) {
        try {
            if (arquivo.isEmpty()) {
                return null;
            }

            Path pastaDestino = this.root.resolve(subpasta);
            if (!Files.exists(pastaDestino)) {
                Files.createDirectories(pastaDestino);
            }

            String extensao = getExtensao(arquivo.getOriginalFilename());
            String nomeArquivo = UUID.randomUUID().toString() + extensao;

            Files.copy(arquivo.getInputStream(), pastaDestino.resolve(nomeArquivo),
                    StandardCopyOption.REPLACE_EXISTING);

            logger.info("Arquivo salvo em {}/{}: {}", subpasta, nomeArquivo, nomeArquivo);
            return nomeArquivo;
        } catch (Exception e) {
            logger.error("Erro ao salvar arquivo na subpasta " + subpasta, e);
            throw new RuntimeException("Erro ao salvar arquivo de imagem", e);
        }
    }

    public void deletar(String nomeArquivo) {
        deletar(nomeArquivo, "usuarios");
    }

    public void deletar(String nomeArquivo, String subpasta) {
        if (nomeArquivo == null || nomeArquivo.isEmpty())
            return;

        try {
            Path arquivo = root.resolve(subpasta).resolve(nomeArquivo);
            Files.deleteIfExists(arquivo);
            logger.info("Arquivo deletado de {}: {}", subpasta, nomeArquivo);
        } catch (IOException e) {
            logger.warn("Não foi possível deletar o arquivo {} em {}: {}", nomeArquivo, subpasta, e.getMessage());
        }
    }

    private String getExtensao(String nomeArquivo) {
        if (nomeArquivo == null || !nomeArquivo.contains("."))
            return ".jpg";
        return nomeArquivo.substring(nomeArquivo.lastIndexOf("."));
    }
}
