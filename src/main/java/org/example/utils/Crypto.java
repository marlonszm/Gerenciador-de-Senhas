package org.example.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// Classe para criptografar e descriptografar senhas usando AES
public class Crypto {
    private static final String CHAVE = "1234567890123456"; // Chave de 16 caracteres (128 bits)

    public static String criptografar(String dado) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(CHAVE.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key); // Inicializa para criptografar
            byte[] encrypted = cipher.doFinal(dado.getBytes()); // Executa criptografia
            return Base64.getEncoder().encodeToString(encrypted); // Retorna base64
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar", e);
        }
    }

    public static String descriptografar(String dadoCriptografado) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec key = new SecretKeySpec(CHAVE.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key); // Inicializa para descriptografar
            byte[] decoded = Base64.getDecoder().decode(dadoCriptografado);
            return new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar", e);
        }
    }
}