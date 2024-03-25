package org.example;

public class Main {
    public static void main(String[] args) {
        // Criando uma instância da classe GerenciadoraContas
        GerenciadoraContas gerContas = new GerenciadoraContas();

        // Adicionando algumas contas correntes
        ContaCorrente conta1 = new ContaCorrente(1, 1000, true);
        ContaCorrente conta2 = new ContaCorrente(2, 2000, true);
        gerContas.adicionarConta(conta1);
        gerContas.adicionarConta(conta2);

        // Realizando uma transferência entre contas
        boolean transferenciaRealizada = gerContas.transferir(1, 2, 500);
        if (transferenciaRealizada) {
            System.out.println("Transferência realizada com sucesso!");
        } else {
            System.out.println("Transferência falhou: saldo insuficiente na conta de origem.");
        }

        // Verificando o saldo de uma conta
        double saldoConta1 = gerContas.verificarSaldo(1);
        System.out.println("Saldo da conta 1: " + saldoConta1);
    }
}
