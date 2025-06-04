package org.example.models;

public final class Credencial {
    private final String servico;
    private final String usuario;
    private final String senhaCriptografada;

    public Credencial(String servico, String usuario, String senhaCriptografada) {
        this.servico = servico;
        this.usuario = usuario;
        this.senhaCriptografada = senhaCriptografada;
    }

    public String getServico() { return servico; }
    public String getUsuario() { return usuario; }
    public String getSenhaCriptografada() { return senhaCriptografada; }

    @Override
    public String toString() {
        return "Serviço: " + servico + ", Usuário: " + usuario + ", Senha: [PROTEGIDA]";
    }
}
