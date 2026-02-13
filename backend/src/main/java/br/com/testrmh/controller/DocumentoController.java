package br.com.testrmh.controller;

import br.com.testrmh.dto.DocumentoRequestDTO;
import br.com.testrmh.dto.DocumentoResponseDTO;
import br.com.testrmh.model.Documento;
import br.com.testrmh.service.DocumentoService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping ("/api/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoResponseDTO> upload(@ModelAttribute @Valid DocumentoRequestDTO request) throws IOException {
        Documento documentoSalvo = documentoService.salvar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DocumentoResponseDTO(documentoSalvo));
    }

    @GetMapping()
    public ResponseEntity<List<DocumentoResponseDTO>> listar() {
        List<DocumentoResponseDTO> lista = documentoService.listarTodos().stream()
                .map(DocumentoResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {
        Resource arquivo = documentoService.carregarArquivo(id);

        String contentType = Files.probeContentType(Paths.get(arquivo.getURI()));
        if (contentType == null) contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getFilename() + "\"")
                .body(arquivo);
    }
}
