package org.example.services;

import org.example.EmailSender;
import org.example.data.BancoSimples;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.security.SecureRandom;

public class Autenticacao2FA {

    // Realiza a autenticação 2FA enviando um código por e-mail
    public static boolean autenticar(String email, String senhaEmail) {
        Scanner sc = new Scanner(System.in);

        String codigo = gerarCodigo();
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(5);
        BancoSimples.salvarOtp(email, codigo, expiracao);

        EmailSender.enviar(email, senhaEmail, email, "Código de verificação 2FA", "Seu código é: " + codigo);

        System.out.print("Digite o código enviado para seu e-mail: ");
        String inserido = sc.nextLine();

        BancoSimples.OtpEntry salvo = BancoSimples.buscarOtp(email);
        if (salvo != null && salvo.expiryTime.isAfter(LocalDateTime.now()) && salvo.otp.equals(inserido)) {
            BancoSimples.removerOtp(email);
            return true;
        }

        return false;
    }

    // Gera um código de 6 dígitos utilizando SecureRandom
    private static String gerarCodigo() {
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(900000) + 100000; // Gera número entre 100000 e 999999
        return String.valueOf(num);
    }
}
