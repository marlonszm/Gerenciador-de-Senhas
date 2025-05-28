package org.example.services;

import org.example.models.Credencial;
import org.example.utils.Crypto;
import java.util.ArrayList;
import java.util.List;

// Classe responsável por gerenciar as credenciais do usuário
public class GerenciadorCredenciais {
    private List<Credencial> lista = new ArrayList<>();

    // Adiciona uma nova credencial criptografando a senha
    public void adicionar(String servico, String usuario, String senha) {
        String senhaCripto = Crypto.criptografar(senha);
        lista.add(new Credencial(servico, usuario, senhaCripto));
    }

    // Retorna todas as credenciais salvas
    public List<Credencial> getCredenciais() {
        return lista;
    }

    // Recupera e descriptografa a senha de um serviço
    public String recuperarSenha(String servico) {
        for (Credencial c : lista) {
            if (c.getServico().equalsIgnoreCase(servico)) {
                return Crypto.descriptografar(c.getSenhaCriptografada());
            }
        }
        return null;
    }
}
