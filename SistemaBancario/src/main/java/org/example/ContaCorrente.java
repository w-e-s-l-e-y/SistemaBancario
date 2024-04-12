package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.example.InterfacePrincipal.atualizarSaldoBancoDados;

public class ContaCorrente {
    private static final double SALDO_MINIMO = 100.0;
    private static final double CHEQUE_ESPECIAL_INICIAL = 100.0;

    private int id;
    private double saldo;
    private double chequeEspecial;
    private boolean ativa;

    public ContaCorrente(int id, double saldoInicial, boolean ativa) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID da conta deve ser maior que zero.");
        }
        this.id = id;
        this.saldo = saldoInicial;
        this.chequeEspecial = CHEQUE_ESPECIAL_INICIAL;
        this.ativa = ativa;
    }

    // Método para abrir a conta com depósito obrigatório
    public static ContaCorrente abrirContaComDeposito(int id, double saldoInicial) {
        if (saldoInicial < SALDO_MINIMO) {
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

    public double getChequeEspecial() {
        return chequeEspecial;
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
        if (valor > saldo + chequeEspecial) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        if (valor > saldo) {
            double valorDoChequeEspecial = valor - saldo;
            saldo = 0;
            chequeEspecial -= valorDoChequeEspecial;
            saldo -= valorDoChequeEspecial; // Reduz o saldo da conta para refletir o uso do cheque especial
            System.out.println("Foram removidos R$" + valorDoChequeEspecial + " do seu valor de cheque especial. Agora você tem R$" + chequeEspecial + " sobrando.");
        } else {
            saldo -= valor;
        }
    }


    public void transferir(ContaCorrente destino, double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta corrente não está ativa.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser transferido não pode ser negativo.");
        }
        if (valor > saldo + chequeEspecial) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }
        if (valor > saldo) {
            double valorDoChequeEspecial = valor - saldo;
            saldo = 0;
            chequeEspecial -= valorDoChequeEspecial;
        } else {
            saldo -= valor;
        }
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
        if (valor > saldo + chequeEspecial) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        // Atualiza o saldo e o cheque especial
        if (saldo < valor) {
            double valorDoChequeEspecial = valor - saldo;
            saldo = 0;
            chequeEspecial -= valorDoChequeEspecial;
        } else {
            saldo -= valor;
        }

        // Atualiza o saldo no banco de dados
        atualizarSaldoBancoDados(id, saldo, chequeEspecial);
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
