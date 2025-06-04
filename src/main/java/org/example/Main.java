package org.example;

import org.example.services.*;
import org.example.utils.*;
import java.io.Console;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Console console = System.console();

        System.out.println("===== LOGIN =====");
        System.out.print("Seu e-mail Gmail: ");
        String email = sc.nextLine();

        String senhaEmail;

        // Para maior segurança, tenta ocultar a senha
        if (console != null) {
            char[] senhaChars = console.readPassword("Senha do app (do Gmail): ");
            senhaEmail = new String(senhaChars);
        } else {
            System.out.print("Senha do app (do Gmail): ");
            senhaEmail = sc.nextLine();  // Fallback, mas menos seguro
        }

        // Autenticação 2FA com envio dinâmico
        if (!Autenticacao2FA.autenticar(email, senhaEmail)) {
            System.out.println("❌ 2FA falhou!");
            return;
        }

        GerenciadorCredenciais manager = new GerenciadorCredenciais();

        // Menu principal
        while (true) {
            try {
                System.out.println("\n1. Adicionar credencial");
                System.out.println("2. Ver serviços salvos");
                System.out.println("3. Gerar senha forte");
                System.out.println("4. Sair");
                System.out.print("Escolha: ");

                String input = sc.nextLine();
                int opcao = Integer.parseInt(input);

                if (opcao == 1) {
                    System.out.print("Serviço: ");
                    String serv = sc.nextLine().trim();
                    System.out.print("Usuário: ");
                    String user = sc.nextLine().trim();

                    String senha;
                    if (console != null) {
                        char[] senhaChars = console.readPassword("Senha: ");
                        senha = new String(senhaChars);
                    } else {
                        System.out.print("Senha: ");
                        senha = sc.nextLine();
                    }

                    if (VerificadorVazamento.foiVazada(senha)) {
                        System.out.println("⚠️ Essa senha já foi vazada!");
                    } else {
                        manager.adicionar(serv, user, senha);
                        System.out.println("✅ Credencial adicionada!");
                    }

                } else if (opcao == 2) {
                    // Exibe apenas os nomes dos serviços armazenados
                    manager.getCredenciais().forEach(c -> {
                        System.out.println("🔐 Serviço: " + c.getServico() + " | Usuário: " + c.getUsuario());
                    });
                    System.out.println("⚠️ As senhas são armazenadas com segurança e não são exibidas diretamente.");

                } else if (opcao == 3) {
                    System.out.println("Senha sugerida: " + GeradorSenha.gerar(12));

                } else if (opcao == 4) {
                    System.out.println("Saindo...");
                    break;
                } else {
                    System.out.println("❌ Opção inválida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida! Por favor, insira um número.");
            } catch (Exception e) {
                System.out.println("❌ Erro inesperado: " + e.getMessage());
            }
        }

        sc.close();
    }
}
