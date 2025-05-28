package org.example;

import org.example.services.*;
import org.example.utils.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== LOGIN =====");
        System.out.print("Seu e-mail Gmail: ");
        String email = sc.nextLine();

        System.out.print("Senha do app (do Gmail): ");
        String senhaEmail = sc.nextLine();  // Para maior segurança, use uma lib que oculta caracteres

        // Autenticação 2FA com envio dinâmico
        if (!Autenticacao2FA.autenticar(email, senhaEmail)) {
            System.out.println("❌ 2FA falhou!");
            return;
        }

        GerenciadorCredenciais manager = new GerenciadorCredenciais();

        // Menu principal
        while (true) {
            System.out.println("\n1. Adicionar credencial");
            System.out.println("2. Ver senhas");
            System.out.println("3. Gerar senha forte");
            System.out.println("4. Sair");
            int opcao = sc.nextInt();
            sc.nextLine();

            if (opcao == 1) {
                System.out.print("Serviço: ");
                String serv = sc.nextLine();
                System.out.print("Usuário: ");
                String user = sc.nextLine();
                System.out.print("Senha: ");
                String senha = sc.nextLine();

                if (VerificadorVazamento.foiVazada(senha)) {
                    System.out.println("⚠️ Essa senha já foi vazada!");
                } else {
                    manager.adicionar(serv, user, senha);
                    System.out.println("✅ Credencial adicionada!");
                }

            } else if (opcao == 2) {
                manager.getCredenciais().forEach(c -> {
                    System.out.println("🔐 " + c.getServico() + ": " + manager.recuperarSenha(c.getServico()));
                });

            } else if (opcao == 3) {
                System.out.println("Senha sugerida: " + GeradorSenha.gerar(12));
            } else {
                break;
            }
        }
    }
}