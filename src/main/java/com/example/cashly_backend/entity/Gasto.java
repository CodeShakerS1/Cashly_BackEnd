package com.example.cashly_backend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gasto")
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Gasto")
    private Integer id_gasto;

    @Column(name = "Valor", nullable = false, precision = 10, scale = 2)
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

    public Integer getId_gasto() { return id_gasto; }
    public void setId_gasto(Integer id_gasto) { this.id_gasto = id_gasto; }

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