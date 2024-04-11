package org.example;


public class ContaCorrente {
    private static final double SALDO_MINIMO = 100.0; // Defina o saldo mínimo aceito para abrir uma conta

    private int id;
    private double saldo;
    private boolean ativa;

    public ContaCorrente(int id, double saldoInicial, boolean ativa) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID da conta deve ser maior que zero.");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        this.id = id;
        this.saldo = saldoInicial;
        this.ativa = ativa;
    }

    // Método para abrir a conta com depósito obrigatório
    public static ContaCorrente abrirContaComDeposito(int id, double saldoInicial) {
        if (saldoInicial < SALDO_MINIMO) { // Verifica se o saldo inicial atende ao limite mínimo
            throw new IllegalArgumentException("O saldo inicial deve ser igual ou superior a " + SALDO_MINIMO);
        }
        return new ContaCorrente(id, saldoInicial, true);
    }
    public int getId() {
        return id;
    }

    public double getSaldo() {
        return saldo;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void depositar(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser depositado não pode ser negativo.");
        }
        saldo += valor;
    }

    public void sacar(double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta corrente não está ativa.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser sacado não pode ser negativo.");
        }
        if (valor > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        saldo -= valor;
    }

    public void transferir(ContaCorrente destino, double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta corrente não está ativa.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser transferido não pode ser negativo.");
        }
        if (valor > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        saldo -= valor;
        destino.depositar(valor);
    }

    public void ativar() {
        ativa = true;
    }

    public void desativar() {
        ativa = false;
    }

    public void debitar(double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta corrente não está ativa.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser debitado não pode ser negativo.");
        }
        if (valor > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        saldo -= valor;
    }

    public void creditar(double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta corrente não está ativa.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser creditado não pode ser negativo.");
        }
        saldo += valor;
    }

    public int getNumero() {
        return id;
    }

    public int getNumeroConta() {
        return id;
    }
}




