package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.example.InterfacePrincipal.atualizarSaldoBancoDados;

public class ContaCorrente {
    private static final double SALDO_MINIMO = 10.0;
    private static final double PROPORCAO_CHEQUE_ESPECIAL = 1.0 / 3.0; // Proporção do saldo inicial para definir o cheque especial

    private int id;
    private double saldo;
    private double chequeEspecial;
    private boolean ativa;
    private double chequeEspecialInicial;
    private double saldoAnterior;

    public ContaCorrente(int id, double saldoInicial, boolean ativa) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID da conta deve ser maior que zero.");
        }
        this.id = id;
        this.saldo = saldoInicial;
        this.chequeEspecialInicial = saldoInicial * PROPORCAO_CHEQUE_ESPECIAL; // Define o cheque especial como um terço do saldo inicial
        this.chequeEspecial = chequeEspecialInicial;
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

    public double getNovoChequeEspecial() {
        // Retorna o valor atual do cheque especial
        return chequeEspecial;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public void depositar(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser depositado não pode ser negativo.");
        }
        saldoAnterior = saldo;
        saldo += valor;
        if (saldoAnterior < 0 && saldo >= 0) {
            // Se o saldo anterior era negativo e o saldo atual se tornou não negativo após o depósito
            // Restaura o cheque especial para o valor inicial
            chequeEspecial = chequeEspecialInicial;
        }
    }
    private void subtrairValorChequeEspecial(double valor) {
        if (valor > 0 && chequeEspecial >= valor) {
            chequeEspecial -= valor;
        } else {
            throw new IllegalArgumentException("Saldo insuficiente no cheque especial.");
        }
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
            JOptionPane.showMessageDialog(null,"Foram removidos R$" + valorDoChequeEspecial + " do seu valor de cheque especial. Agora você tem R$" + chequeEspecial + " de cheque sobrando.");
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

        // Se o valor for maior que o saldo + cheque especial, lança uma exceção
        if (valor > saldo + chequeEspecial) {
            throw new IllegalArgumentException("Saldo insuficiente.");
        }

        // Primeiro, verifica se o saldo é suficiente para o débito
        if (valor <= saldo) {
            saldo -= valor;
        } else {
            // Se não for, utiliza o cheque especial
            double valorDoChequeEspecial = valor - saldo;
            if (valorDoChequeEspecial <= chequeEspecial) {
                subtrairValorChequeEspecial(valorDoChequeEspecial);
                saldo = 0;
            } else {
                throw new IllegalArgumentException("Saldo insuficiente no cheque especial.");
            }
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
