package br.com.testrmh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "Comentarios")
public class Comentario {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String comentario;
    private LocalDateTime dataHoraComentario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id")
    @JsonIgnore //Para evitar buscas n+1 nos documentos
    private Documento documento;


    //Construtor vazio para o JPA
    public Comentario() {
    }

    public Comentario(String comentario, Documento documento) {
        this.comentario = comentario;
        this.documento = documento;
        this.dataHoraComentario = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataHoraComentario() {
        return dataHoraComentario;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "id=" + id +
                ", comentario='" + comentario + '\'' +
                ", dataHoraComentario=" + dataHoraComentario +
                ", documento=" + (documento != null ? documento.getTitulo() : "Sem documento") +
                '}';
    }
}
