package com.example.cashly_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Integer id;

    @Column(name = "username", length = 100, nullable = false)
    private String name;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "userpassword", length = 100, nullable = false)
    private String password;

    @Column(name = "photo", length = 255)
    private String photo;

    public User() {
    }

    public User(Integer id, String name, String email, String password, String photo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.photo = photo;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
}