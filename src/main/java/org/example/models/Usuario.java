package org.example.models;

public final class Usuario {
    private final String email;
    private final String senhaHash;

    public Usuario(String email, String senhaHash) {
        this.email = email;
        this.senhaHash = senhaHash;
    }

    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }

    @Override
    public String toString() {
        return "E-mail: " + email + ", SenhaHash: [PROTEGIDA]";
    }
}
