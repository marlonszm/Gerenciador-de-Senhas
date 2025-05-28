# 🔐 Gerenciador de Senhas com 2FA via E-mail

Este é um projeto Java simples de terminal que implementa um **Gerenciador de Senhas com Autenticação de Dois Fatores (2FA)** via **e-mail**, além de funcionalidades como geração de senhas fortes e verificação de vazamento.

## ✨ Funcionalidades

- ✅ Autenticação 2FA: código de verificação enviado por e-mail.
- 🔑 Armazenamento de senhas criptografadas.
- 🔍 Verificação se a senha foi vazada (simulado).
- 💡 Geração de senhas fortes e seguras.
- 🧠 Menu interativo simples em terminal.

---

## 🚀 Como executar

### 1. Clone o projeto
```
bash
git clone https://github.com/seu-usuario/gerenciador-senhas.git
cd gerenciador-senhas
```
### 2. Compile e execute

Se estiver usando Maven:
```
mvn compile
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Ou via terminal diretamente (se não estiver usando Maven):
```
javac -cp ".:libs/*" -d bin src/org/example/**/*.java
java -cp ".:bin:libs/*" org.example.Main
```

### 📥 Entrada de E-mail e Senha
Ao iniciar o sistema, você será solicitado a informar:

Seu e-mail Gmail

A senha do app ou do seu Gmail (⚠️ Veja abaixo)

O sistema enviará um código de 6 caracteres para o e-mail informado. Digite esse código corretamente para acessar o menu do gerenciador.

### ⚠️ Sobre a Senha do Gmail
Por segurança e restrições do Google, muitas vezes o envio de e-mails não funciona com sua senha normal do Gmail.

✅ Solução: Gere uma Senha de Aplicativo
Ative a verificação em duas etapas em https://myaccount.google.com/security

Acesse: https://myaccount.google.com/apppasswords

Escolha a opção “Outro”, digite algo como "GerenciadorSenhas" e clique em Gerar.

Copie e use a senha gerada ao rodar o programa.

💡 Essa senha é exclusiva para o programa e pode ser revogada a qualquer momento.

### 🔐 Segurança
Todas as senhas de serviços armazenadas são criptografadas.

O OTP (código de autenticação 2FA) tem validade de 5 minutos.

O sistema armazena tudo localmente (simulação em memória).

🛠 Tecnologias utilizadas
Java 8+

JavaMail API (javax.mail)

UUID para geração de OTPs

Criptografia AES

Terminal interativo


---

Se quiser que eu gere esse `README.md` automaticamente como arquivo ou já inclua no seu projeto ZIP, posso fazer isso também.
