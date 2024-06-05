package org.example;

import javax.swing.*;

public class ContaCorrente {
    private static final double SALDO_MINIMO = 10.0;
    private static final double PROPORCAO_CHEQUE_ESPECIAL = 1.0 / 3.0;

    private int id;
    private double saldo;
    private double chequeEspecial;
    private boolean ativa;
    private double chequeEspecialInicial;
    private double saldoAnterior;
    private RealtimeDatabase realtimeDatabase;

    public void setChequeEspecial(double chequeEspecial) {
        this.chequeEspecial = chequeEspecial;
    }

    public ContaCorrente(int id, double saldoInicial, boolean ativa) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID da conta deve ser maior que zero.");
        }
        this.id = id;
        this.saldo = saldoInicial;
        this.chequeEspecialInicial = saldoInicial * PROPORCAO_CHEQUE_ESPECIAL;
        this.chequeEspecial = chequeEspecialInicial;
        this.ativa = ativa;
        this.realtimeDatabase = new RealtimeDatabase(); // Inicialize o RealtimeDatabase aqui
    }

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
            chequeEspecial = chequeEspecialInicial;
        }
        atualizarSaldoBancoDados();
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
            saldo -= valorDoChequeEspecial;
            JOptionPane.showMessageDialog(null, "Foram removidos R$" + valorDoChequeEspecial + " do seu valor de cheque especial. Agora você tem R$" + chequeEspecial + " de cheque sobrando.");
        } else {
            saldo -= valor;
        }
        atualizarSaldoBancoDados();
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
        atualizarSaldoBancoDados();
    }

    public void ativar() {
        ativa = true;
        atualizarSaldoBancoDados();
    }

    public void desativar() {
        ativa = false;
        atualizarSaldoBancoDados();
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
        if (valor <= saldo) {
            saldo -= valor;
        } else {
            double valorDoChequeEspecial = valor - saldo;
            if (valorDoChequeEspecial <= chequeEspecial) {
                subtrairValorChequeEspecial(valorDoChequeEspecial);
                saldo = 0;
            } else {
                throw new IllegalArgumentException("Saldo insuficiente no cheque especial.");
            }
        }
        atualizarSaldoBancoDados();
    }

    public void creditar(double valor) {
        if (!ativa) {
            throw new IllegalStateException("Conta corrente não está ativa.");
        }
        if (valor < 0) {
            throw new IllegalArgumentException("O valor a ser creditado não pode ser negativo.");
        }
        saldo += valor;
        atualizarSaldoBancoDados();
    }

    public int getNumero() {
        return id;
    }

    public int getNumeroConta() {
        return id;
    }

    private void atualizarSaldoBancoDados() {
        realtimeDatabase.atualizarSaldoBancoDados(id, saldo, chequeEspecial);
    }
}
