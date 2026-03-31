package com.example.cashly_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table (name = "receita")
public class Receita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Receita")
    private Integer id_receita;

    @Column(name = "Valor", nullable = false, precision = 10, scale = 2 )
    private BigDecimal valor;

    @Column(name = "Descricao", length = 100)
    private String descricao;

    @Column(name = "Metodo", length = 50)
    private String metodo;

    @Column(name = "Data", nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "Id_Categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "Id_Usuario", nullable = false)
    private Usuario usuario;

    public Integer getId_receita() { return id_receita; }
    public void setId_receita(Integer id_receita) { this.id_receita = id_receita; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    
}
