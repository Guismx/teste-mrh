package br.com.testrmh.dto;

import br.com.testrmh.model.Comentario;
import java.time.LocalDateTime;

public record ComentarioResponseDTO(
        Long id,
        String comentario,
        LocalDateTime dataHoraComentario,
        Long documentoId //ID para saber a quem pertence
) {
    //Construtor auxiliar para converter a Entidade em DTO
    public ComentarioResponseDTO(Comentario comentario) {
        this(
                comentario.getId(),
                comentario.getComentario(),
                comentario.getDataHoraComentario(),
                comentario.getDocumento().getId()
        );
    }
}