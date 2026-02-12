package br.com.testrmh.controller;

import br.com.testrmh.dto.ComentarioRequestDTO;
import br.com.testrmh.dto.ComentarioResponseDTO;
import br.com.testrmh.model.Comentario;
import br.com.testrmh.service.ComentarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documentos/{documentoId}/comentarios")
public class ComentarioController {

    private final ComentarioService service;

    public ComentarioController(ComentarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> adicionar(
            @PathVariable Long documentoId,
            @RequestBody @Valid ComentarioRequestDTO request) {

        Comentario novoComentario = service.adicionar(documentoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ComentarioResponseDTO(novoComentario));
    }

    @GetMapping
    public ResponseEntity<List<ComentarioResponseDTO>> listar(@PathVariable Long documentoId) {
        List<ComentarioResponseDTO> lista = service.listarPorDocumento(documentoId).stream()
                .map(ComentarioResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }
}