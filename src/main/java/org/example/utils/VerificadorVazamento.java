package org.example.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

// Classe para verificar se a senha foi vazada usando a API do Have I Been Pwned
public class VerificadorVazamento {

    public static boolean foiVazada(String senha) {
        HttpURLConnection con = null;
        try {
            // Gera o hash SHA-1 da senha
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(senha.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02X", b));

            String hashCompleto = sb.toString();
            String prefixo = hashCompleto.substring(0, 5);  // Primeiro 5 caracteres do hash
            String sufixo = hashCompleto.substring(5);     // Resto do hash

            // Consulta API com o prefixo
            URL url = new URL("https://api.pwnedpasswords.com/range/" + prefixo);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            int status = con.getResponseCode();
            if (status != 200) {
                throw new RuntimeException("Erro na API: status " + status);
            }

            // Lê a resposta e verifica se o sufixo está presente
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String linha;
            while ((linha = in.readLine()) != null) {
                if (linha.startsWith(sufixo)) {
                    in.close();
                    return true;
                }
            }
            in.close();
            return false; // Senha não encontrada nos vazamentos
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar vazamento", e);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
