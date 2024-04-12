package org.example;

public class Conta {
    private int numero;
    private double saldo;
    private static final double LIMITE_CHEQUE_ESPECIAL = 100.0; // Definindo o limite do cheque especial

    public Conta(int numero, double saldoInicial) {
        if (numero <= 0) {
            throw new IllegalArgumentException("O número da conta deve ser maior que zero.");
        }
        if (saldoInicial < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser negativo.");
        }
        this.numero = numero;
        this.saldo = saldoInicial + LIMITE_CHEQUE_ESPECIAL; // Adicionando o limite do cheque especial ao saldo inicial
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
            // Se o valor for maior que o saldo, verificar se é possível usar o cheque especial
            double valorDisponivel = saldo + LIMITE_CHEQUE_ESPECIAL;
            if (valor > valorDisponivel) {
                throw new IllegalArgumentException("Saldo insuficiente para debitar o valor solicitado.");
            } else {
                // Usando o cheque especial
                saldo = 0; // Zerando o saldo
                double valorChequeEspecial = valor - saldo; // Calculando o valor usado do cheque especial
                System.out.println("Você retirou R$ " + valorChequeEspecial + " do cheque especial."); // Mensagem de confirmação
            }
        } else {
            // Se houver saldo suficiente, realizar o saque normalmente
            saldo -= valor;
        }
    }

    public void retirarChequeEspecial(double valor) {
        // Calcula o limite do cheque especial
        double valorDisponivel = saldo + LIMITE_CHEQUE_ESPECIAL;

        // Verifica se o valor de retirada excede o limite disponível
        if (valor > valorDisponivel) {
            throw new IllegalArgumentException("Valor de retirada excede o limite disponível.");
        }

        // Realiza a retirada do valor solicitado
        saldo -= valor;

        // Retorna o valor retirado em formato negativo se o saldo ficar negativo
        if (saldo < 0) {
            // TODO: Tratar o saldo negativo, se necessário
        }
    }
}
