package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    // Envia um e-mail simples via SMTP
    public static void enviar(String usuarioEmail, String senhaEmail, String destino, String assunto, String corpo) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session sessao = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuarioEmail, senhaEmail);
            }
        });

        try {
            Message mensagem = new MimeMessage(sessao);
            mensagem.setFrom(new InternetAddress(usuarioEmail));
            mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            mensagem.setSubject(assunto);
            mensagem.setText(corpo);

            Transport.send(mensagem);
            System.out.println("✅ Código enviado para: " + destino);

        } catch (MessagingException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}
