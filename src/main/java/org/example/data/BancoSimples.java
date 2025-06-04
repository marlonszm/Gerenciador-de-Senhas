package org.example.data;

import org.example.models.Credencial;
import org.example.models.Usuario;
import org.example.utils.Crypto;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.LocalDateTime;

public class BancoSimples {

    // Armazena os usuários com e-mail como chave
    private static final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    // Armazena as credenciais de cada usuário
    private static final Map<String, Map<String, Credencial>> credenciaisPorUsuario = new ConcurrentHashMap<>();

    // Armazena códigos OTP temporários (2FA) com expiração
    private static final ConcurrentHashMap<String, OtpEntry> otpsAtivos = new ConcurrentHashMap<>();

    // Classe interna para representar um código OTP e seu tempo de expiração
    public static class OtpEntry {
        public final String otp;
        public final LocalDateTime expiryTime;

        public OtpEntry(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }
    }

    // Salva o OTP associado a um e-mail
    public static void salvarOtp(String userEmail, String otp, LocalDateTime expiryTime) {
        if (userEmail == null || otp == null || expiryTime == null) return;
        otpsAtivos.put(userEmail, new OtpEntry(otp, expiryTime));
    }

    // Busca o OTP associado a um e-mail, removendo se expirado
    public static OtpEntry buscarOtp(String userEmail) {
        if (userEmail == null) return null;
        OtpEntry entry = otpsAtivos.get(userEmail);
        if (entry != null && LocalDateTime.now().isAfter(entry.expiryTime)) {
            otpsAtivos.remove(userEmail);
            return null;
        }
        return entry;
    }

    // Remove o OTP após o uso ou expiração
    public static void removerOtp(String userEmail) {
        if (userEmail == null) return;
        otpsAtivos.remove(userEmail);
    }

    // Salva um novo usuário
    public static void salvarUsuario(Usuario usuario) {
        if (usuario == null || usuario.getEmail() == null) return;
        usuarios.putIfAbsent(usuario.getEmail(), usuario);
        credenciaisPorUsuario.putIfAbsent(usuario.getEmail(), new ConcurrentHashMap<>());
    }

    // Recupera um usuário pelo e-mail
    public static Usuario buscarUsuario(String email) {
        if (email == null) return null;
        return usuarios.get(email);
    }

    // Salva uma nova credencial para um usuário, criptografando a senha
    public static void salvarCredencial(String emailUsuario, Credencial credencial) {
        if (emailUsuario == null || credencial == null || credencial.getServico() == null) return;

        Map<String, Credencial> credenciais = credenciaisPorUsuario.get(emailUsuario);
        if (credenciais != null) {
            // Criptografa a senha antes de armazenar
            String senhaCriptografada = Crypto.criptografar(credencial.getSenhaCriptografada());

            // Cria nova instância com a senha criptografada
            Credencial credencialCriptografada = new Credencial(
                    credencial.getServico(),
                    credencial.getUsuario(),
                    senhaCriptografada
            );

            credenciais.put(credencialCriptografada.getServico(), credencialCriptografada);
        }
    }

    // Recupera as credenciais de um usuário
    public static Map<String, Credencial> buscarCredenciais(String emailUsuario) {
        if (emailUsuario == null) return null;
        return credenciaisPorUsuario.get(emailUsuario);
    }

    // Recupera e descriptografa a senha de uma credencial específica
    public static String recuperarSenhaDescriptografada(String emailUsuario, String servico) {
        if (emailUsuario == null || servico == null) return null;

        Map<String, Credencial> credenciais = credenciaisPorUsuario.get(emailUsuario);
        if (credenciais != null) {
            Credencial credencial = credenciais.get(servico);
            if (credencial != null) {
                return Crypto.descriptografar(credencial.getSenhaCriptografada());
            }
        }
        return null;
    }
}
