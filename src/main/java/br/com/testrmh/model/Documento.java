package br.com.testrmh.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "Documentos")
public class Documento {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private String nomeArquivo;
    private String caminhoArquivo;
    private LocalDateTime dataUpload;

    //Relacionamento com a entidade Comentarios
    @OneToMany (mappedBy = "documento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comentario> comentarios = new ArrayList<>();

    //Construtor vazio para o JPA
    public Documento () {
    }

    public Documento(String titulo, String descricao, String nomeArquivo, String caminhoArquivo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.nomeArquivo = nomeArquivo;
        this.caminhoArquivo = caminhoArquivo;
        this.dataUpload = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "Documento{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", nomeArquivo='" + nomeArquivo + '\'' +
                ", caminhoArquivo='" + caminhoArquivo + '\'' +
                ", dataUpload=" + dataUpload +
                '}';
    }
}
