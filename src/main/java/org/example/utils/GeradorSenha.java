package org.example.utils;

import java.security.SecureRandom;

// Classe utilitária para gerar senhas fortes aleatórias
public class GeradorSenha {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";

    public static String gerar(int tamanho) {
        SecureRandom rand = new SecureRandom(); // Gera aleatoriedade com segurança
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < tamanho; i++) {
            int index = rand.nextInt(CHARS.length());
            sb.append(CHARS.charAt(index));
        }
        return sb.toString(); // Retorna a senha gerada
    }
}