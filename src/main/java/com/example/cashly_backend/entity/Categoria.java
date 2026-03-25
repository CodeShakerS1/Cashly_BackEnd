package com.example.cashly_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Categoria")
    private Integer id_categoria;

    @Column(name = "Nome", length = 50, nullable = false)
    private String nome;

    @Column(name = "Icone", length = 100)
    private String icone;

    @Pattern(regexp = "receita|despesa", message = "Tipo deve ser 'receita' ou 'despesa'")
    @Column(name = "Tipo", length = 15)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "Id_Usuario", nullable = false)
    private Usuario usuario;

    public Integer getId_categoria() { return id_categoria; }
    public void setId_categoria(Integer id_categoria) { this.id_categoria = id_categoria; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIcone() { return icone; }
    public void setIcone(String icone) { this.icone = icone; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}