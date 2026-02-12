package br.com.testrmh.service;

import br.com.testrmh.dto.ComentarioRequestDTO;
import br.com.testrmh.model.Comentario;
import br.com.testrmh.model.Documento;
import br.com.testrmh.repository.ComentarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final DocumentoService documentoService;

    public ComentarioService(ComentarioRepository comentarioRepository, DocumentoService documentoService) {
        this.comentarioRepository = comentarioRepository;
        this.documentoService = documentoService;
    }

    public Comentario adicionar(Long documentoId, ComentarioRequestDTO dto) {
        Documento documento = documentoService.buscarPorId(documentoId);

        Comentario novoComentario = new Comentario(dto.comentario(), documento);

        return comentarioRepository.save(novoComentario);
    }

    public List<Comentario> listarPorDocumento(Long documentoId) {
        Documento documento = documentoService.buscarPorId(documentoId);
        return documento.getComentarios();
    }
}
