package com.example.cashly_backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Usuario")
    private Integer id_usuario;

    @Column(name = "Nome", length = 100, nullable = false)
    private String nome;

    @JsonIgnore
    @Column(name = "Email", length = 100, nullable = false)
    private String email;

    @JsonIgnore
    @Column(name = "Senha", length = 255, nullable = false)
    private String senha;
    
    public Integer getId_usuario() { return id_usuario; }
    public void setId_usuario(Integer id_usuario) { this.id_usuario = id_usuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}