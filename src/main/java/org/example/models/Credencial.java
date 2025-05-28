package org.example.models;

// Representa uma credencial (serviço, usuário e senha criptografada)
public class Credencial {
    private String servico;
    private String usuario;
    private String senhaCriptografada;

    public Credencial(String servico, String usuario, String senhaCriptografada) {
        this.servico = servico;
        this.usuario = usuario;
        this.senhaCriptografada = senhaCriptografada;
    }

    public String getServico() { return servico; }
    public String getUsuario() { return usuario; }
    public String getSenhaCriptografada() { return senhaCriptografada; }
}