package org.example.services;

import org.example.EmailSender;
import org.example.data.BancoSimples;
import org.example.models.Usuario;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
import java.util.UUID;
import org.example.EmailSender;
import org.mindrot.jbcrypt.BCrypt;

public class Autenticacao2FA {

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

    private static String gerarCodigo() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
