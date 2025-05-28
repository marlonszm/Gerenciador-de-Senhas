package org.example.data;

import org.example.models.Credencial;
import org.example.models.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class BancoSimples {

    // Armazena os usuários com e-mail como chave
    private static final Map<String, Usuario> usuarios = new HashMap<>();

    // Armazena as credenciais de cada usuário
    private static final Map<String, Map<String, Credencial>> credenciaisPorUsuario = new HashMap<>();

    // Armazena códigos OTP temporários (2FA) com expiração
    private static final ConcurrentHashMap<String, OtpEntry> otpsAtivos = new ConcurrentHashMap<>();

    // Classe interna para representar um código OTP e seu tempo de expiração
    public static class OtpEntry {
        public String otp;
        public LocalDateTime expiryTime;

        public OtpEntry(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }

    // Salva o OTP associado a um e-mail
    public static void salvarOtp(String userEmail, String otp, LocalDateTime expiryTime) {
        otpsAtivos.put(userEmail, new OtpEntry(otp, expiryTime));
    }

    // Busca o OTP associado a um e-mail
    public static OtpEntry buscarOtp(String userEmail) {
        return otpsAtivos.get(userEmail);
    }

    // Remove o OTP após o uso ou expiração
    public static void removerOtp(String userEmail) {
        otpsAtivos.remove(userEmail);
    }

    // Salva um novo usuário
    public static void salvarUsuario(Usuario usuario) {
        usuarios.put(usuario.getEmail(), usuario);
        credenciaisPorUsuario.put(usuario.getEmail(), new HashMap<>());
    }

    // Recupera um usuário pelo e-mail
    public static Usuario buscarUsuario(String email) {
        return usuarios.get(email);
    }

    // Salva uma nova credencial para um usuário
    public static void salvarCredencial(String emailUsuario, Credencial credencial) {
        Map<String, Credencial> credenciais = credenciaisPorUsuario.get(emailUsuario);
        if (credenciais != null) {
            credenciais.put(credencial.getServico(), credencial);
        }
    }

    // Recupera as credenciais de um usuário
    public static Map<String, Credencial> buscarCredenciais(String emailUsuario) {
        return credenciaisPorUsuario.get(emailUsuario);
    }
}