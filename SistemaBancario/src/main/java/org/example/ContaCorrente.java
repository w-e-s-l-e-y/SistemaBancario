package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContaCorrente {
    private static final double SALDO_MINIMO = 100.0;

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

    public static ContaCorrente abrirContaComDeposito(int id, double saldoInicial, Connection connection) throws SQLException {
        if (saldoInicial < SALDO_MINIMO) {
            throw new IllegalArgumentException("O saldo inicial deve ser igual ou superior a " + SALDO_MINIMO);
        }

        ContaCorrente conta = new ContaCorrente(id, saldoInicial, true);
        conta.salvarNoBanco(connection);
        return conta;
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

    public void salvarNoBanco(Connection connection) throws SQLException {
        String sql = "INSERT INTO ContaCorrente (id, saldo, ativa) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setDouble(2, saldo);
            statement.setBoolean(3, ativa);
            statement.executeUpdate();
        }
    }

    public static ContaCorrente carregarDoBanco(int id, Connection connection) throws SQLException {
        String sql = "SELECT * FROM ContaCorrente WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double saldo = resultSet.getDouble("saldo");
                    boolean ativa = resultSet.getBoolean("ativa");
                    return new ContaCorrente(id, saldo, ativa);
                }
            }
        }
        return null;
    }
}
