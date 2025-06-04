package org.example.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

// Classe para criptografar e descriptografar senhas usando AES
public class Crypto {
    // Chave de 16 caracteres (128 bits) — NÃO expor em produção
    private static final String CHAVE = "1234567890123456";

    // Método para criptografar dados usando AES
    public static String criptografar(String dado) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Definido explicitamente
            SecretKeySpec key = new SecretKeySpec(CHAVE.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key); // Inicializa para criptografar
            byte[] encrypted = cipher.doFinal(dado.getBytes(StandardCharsets.UTF_8)); // Consistente charset
            return Base64.getEncoder().encodeToString(encrypted); // Retorna base64
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar", e);
        }
    }

    // Método para descriptografar dados usando AES
    public static String descriptografar(String dadoCriptografado) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // Definido explicitamente
            SecretKeySpec key = new SecretKeySpec(CHAVE.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key); // Inicializa para descriptografar
            byte[] decoded = Base64.getDecoder().decode(dadoCriptografado);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar", e);
        }
    }
}
