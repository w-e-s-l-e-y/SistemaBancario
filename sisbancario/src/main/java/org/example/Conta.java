package org.example;

public class Conta {
    private int numero;
    private double saldo;

    public Conta(int numero, double saldoInicial) {
        if (numero <= 0) {
            throw new IllegalArgumentException("O número da conta deve ser maior que zero.");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        this.numero = numero;
        this.saldo = saldoInicial;
    }

    public int getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void creditar(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser creditado não pode ser negativo.");
        }
        saldo += valor;
    }

    public void debitar(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser debitado não pode ser negativo.");
        }
        if (valor > saldo) {
            throw new IllegalArgumentException("Saldo insuficiente para debitar o valor solicitado.");
        }
        saldo -= valor;
    }
}
