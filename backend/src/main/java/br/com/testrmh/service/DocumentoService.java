package br.com.testrmh.service;

import br.com.testrmh.dto.DocumentoRequestDTO;
import br.com.testrmh.model.Documento;
import br.com.testrmh.repository.DocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.net.MalformedURLException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final Path diretorioUploads = Paths.get("uploads");

    public DocumentoService(DocumentoRepository documentoRepository) {
        this.documentoRepository = documentoRepository;

        try {
            Files.createDirectories(diretorioUploads);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar diretório de uploads", e);
        }
    }

    public Documento salvar(DocumentoRequestDTO dto) throws IOException{
        MultipartFile arquivo = dto.arquivo();
        String nomeOriginal = arquivo.getOriginalFilename();

        String nomeArquivoSalvo = UUID.randomUUID().toString() + "_" + nomeOriginal;

        Path caminhoCompleto = diretorioUploads.resolve(nomeArquivoSalvo);

        Files.copy(arquivo.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

        Documento novoDoc = new Documento();
        novoDoc.setTitulo(dto.titulo());
        novoDoc.setDescricao(dto.descricao());
        novoDoc.setNomeArquivo(nomeOriginal);
        novoDoc.setCaminhoArquivo(caminhoCompleto.toString());

        return documentoRepository.save(novoDoc);
    }

    public Resource carregarArquivo(Long id) {
        try {
            Documento documento = buscarPorId(id);

            Path arquivoPath = Paths.get(documento.getCaminhoArquivo());

            Resource resource = new UrlResource(arquivoPath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Não foi possível ler o arquivo: " + documento.getNomeArquivo());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erro: Caminho do arquivo inválido", e);
        }
    }

    public List<Documento> listarTodos(){
        return documentoRepository.findAll();
    }

    public Documento buscarPorId(Long id) {
        return documentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento não encontrado!"));
    }
}
