package org.example;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class GerenciadorDeSenhas {

    private static final String CHAVE_AES = "1234567890123456";
    private static final String ARQUIVO_SENHAS = "senhas.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao Gerenciador de Senhas 🔐");

        if (!verificarCodigo2FA(scanner)) {
            System.out.println("Autenticação de dois fatores falhou.");
            scanner.close();
            return;
        }

        System.out.print("Digite o nome do serviço: ");
        String nomeDoServico = scanner.nextLine();

        String senhaUsuario;

        while (true) {
            senhaUsuario = solicitarSenha(scanner);

            System.out.println("Verificando vazamento da senha...");

            if (senhaEstaProtegida(senhaUsuario)) {
                System.out.println("Senha considerada segura.");
                break;
            } else {
                System.out.println("Senha comprometida. Escolha outra.\n");
            }
        }

        try {
            String senhaCriptografada = criptografarComAES(senhaUsuario, CHAVE_AES);
            armazenarSenha(nomeDoServico, senhaCriptografada);
            System.out.println("Senha armazenada com sucesso para: " + nomeDoServico);
        } catch (Exception e) {
            System.out.println("❌ Erro ao salvar: " + e.getMessage());
        }

        scanner.close();
    }

    private static String solicitarSenha(Scanner scanner) {
        System.out.print("Deseja que o sistema gere uma senha segura? (sim/não): ");
        String resposta = scanner.nextLine();

        if (resposta.equalsIgnoreCase("sim")) {
            String senhaAleatoria = gerarSenhaAleatoriaSegura(12);
            System.out.println("Senha gerada: " + senhaAleatoria);
            return senhaAleatoria;
        } else {
            System.out.print("Digite sua senha: ");
            return scanner.nextLine();
        }
    }

    private static boolean senhaEstaProtegida(String senha) {
        try {
            return !consultaVazamentoPwned(senha);
        } catch (IOException e) {
            System.out.println("Falha na verificação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
        return false;
    }

    public static String gerarSenhaAleatoriaSegura(int tamanho) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            int idx = random.nextInt(chars.length());
            senha.append(chars.charAt(idx));
        }
        return senha.toString();
    }

    public static boolean verificarCodigo2FA(Scanner scanner) {
        System.out.print("Digite o código 2FA (123456 para teste): ");
        return scanner.nextLine().equals("123456");
    }

    public static boolean consultaVazamentoPwned(String senha) throws Exception {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] hash = sha1.digest(senha.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02X", b));
        String hashHex = sb.toString();

        String prefixo = hashHex.substring(0, 5);
        String sufixo = hashHex.substring(5);

        String url = "https://api.pwnedpasswords.com/range/" + prefixo;

        URI uri = URI.create(url);
        HttpURLConnection conexao = (HttpURLConnection) uri.toURL().openConnection();
        conexao.setRequestMethod("GET");
        conexao.setRequestProperty("User-Agent", "Java Password Manager");

        int responseCode = conexao.getResponseCode();
        if (responseCode != 200) throw new IOException("Erro HTTP: " + responseCode);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conexao.getInputStream()))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(":");
                if (partes[0].equalsIgnoreCase(sufixo)) return true;
            }
        }
        return false;
    }

    public static String criptografarComAES(String texto, String chave) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(chave.getBytes(StandardCharsets.UTF_8), "AES");

        // Gera IV aleatório (substituir IV fixo por este em produção)
        IvParameterSpec iv = new IvParameterSpec(new byte[16]);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        byte[] resultado = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(resultado);
    }

    public static void armazenarSenha(String servico, String senhaCriptografada) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_SENHAS, true))) {
            writer.write(servico + ";" + senhaCriptografada + "\n");
        }
    }
}