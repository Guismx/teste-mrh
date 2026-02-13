package br.com.testrmh.dto;

import jakarta.validation.constraints.NotBlank;

public record ComentarioRequestDTO(
        @NotBlank(message = "O texto do comentário é obrigatório")
        String comentario
) {
}
