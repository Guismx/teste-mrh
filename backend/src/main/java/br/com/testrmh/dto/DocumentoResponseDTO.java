package br.com.testrmh.dto;

import br.com.testrmh.model.Documento;

import java.time.LocalDateTime;

public record DocumentoResponseDTO(
        Long id,
        String titulo,
        String descricao,
        String nomeArquivo,
        LocalDateTime dataUpload,
        String urlDownload
) {
    //Construtor auxiliar para converter a Entidade em DTO
    public DocumentoResponseDTO(Documento doc) {
        this(
                doc.getId(),
                doc.getTitulo(),
                doc.getDescricao(),
                doc.getNomeArquivo(),
                doc.getDataUpload(),
                "/api/documentos/" + doc.getId() + "/download" //Gera a URL
        );
    }
}
