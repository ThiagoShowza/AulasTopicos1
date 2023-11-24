package br.unitins.topicos1.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioFileService implements FileService {

    // /users/nomeusuario
    private final String PATH_USER = System.getProperty("user.home") +
            File.separator + "quarkus" +
            File.separator + "images" +
            File.separator + "usuario" +
            File.separator;

    // "Tipos" de arquivos suportados pelo sistema
    private static final List<String> SUPPORTED_MIME_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png",
            "image/gif");

    private static final int MAX_FILE_SIZE = 1024 * 1024 * 10;

    @Override
    public String salvar(String nomeArquivo, byte[] arquivo) throws IOException {
        verificarTamanhoImagem(arquivo);
        verificarTipoImagem(nomeArquivo);

        // Cria o diretorio caso ele não exista
        Path diretorio = Paths.get(PATH_USER);
        Files.createDirectories(diretorio);

        // criando nome do arquivo
        String mimeType = Files.probeContentType(Paths.get(nomeArquivo));
        String extensao = mimeType.substring(mimeType.lastIndexOf('/') + 1);
        String novoNomeArquivo = UUID.randomUUID() + "." + extensao;

        // Define o caminho completo do arquivo
        Path filePath = diretorio.resolve(novoNomeArquivo);

        if (filePath.toFile().exists())
            throw new IOException("Arquivo ja existe. Os alunos vão buscar outro arquivo.");

        // Abre o arquivo dentro do Try
        // FileOutputStream -> Cria uma saida para os dados do arquivo e salva no local
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            fos.write(arquivo);// Salva o arquivo localmente
        }

        return filePath.toFile().getName();
    }

    @Override
    public File obter(String nomeArquivo) {
        // Busca o arquivo em uma pasta e retorna ele
        // Nesse caso o arquivo ja existe
        File file = new File(PATH_USER + nomeArquivo);

        return null;
    }

    private void verificarTamanhoImagem(byte[] arquivo) throws IOException {
        if (arquivo.length > MAX_FILE_SIZE)
            throw new IOException("Arquivo maior que 10MB");
    }

    private void verificarTipoImagem(String nomeArquivo) throws IOException {
        String mimeType = Files.probeContentType(Paths.get(nomeArquivo));// ira retornar a estrutura com os tipos de
                                                                         // arquivos.
        if (!SUPPORTED_MIME_TYPES.contains(mimeType)) {
            throw new IOException("Tipo de imagem não suportada");
        }
    }

}
