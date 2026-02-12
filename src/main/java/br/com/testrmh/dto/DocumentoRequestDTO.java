package br.com.testrmh.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record DocumentoRequestDTO (
        @NotBlank (message = "Necessário informar o Titulo do Documento")
        String titulo,
        String descricao,

        @NotNull(message = "É obrigatório anexar um arquivo")
        MultipartFile arquivo
){
}
