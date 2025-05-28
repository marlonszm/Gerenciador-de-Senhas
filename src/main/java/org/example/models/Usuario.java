package org.example.models;

// Representa um usuário com email e senha (armazenada como hash)
public class Usuario {
    private String email;
    private String senhaHash;

    public Usuario(String email, String senhaHash) {
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
}