package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadoraContas {
    private Connection connection;

    public GerenciadoraContas(Connection connection) {
        this.connection = connection;
    }

    // Método para adicionar uma nova conta corrente
    public void adicionarConta(ContaCorrente conta) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO ContaCorrente (id, saldo, ativa) VALUES (?, ?, ?)")) {
            statement.setInt(1, conta.getId());
            statement.setDouble(2, conta.getSaldo());
            statement.setBoolean(3, conta.isAtiva());
            statement.executeUpdate();
        }
    }


    // Método para remover uma conta corrente
    public boolean removerConta(int numeroConta) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ContaCorrente WHERE id = ?")) {
            statement.setInt(1, numeroConta);
            return statement.executeUpdate() > 0;
        }
    }

    // Método para verificar o saldo de uma conta corrente
    public double verificarSaldo(int numeroConta) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT saldo FROM ContaCorrente WHERE id = ?")) {
            statement.setInt(1, numeroConta);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("saldo");
                }
            }
        }
        return -1; // Conta não encontrada
    }

    // Método para transferir dinheiro entre contas correntes
    public boolean transferir(int contaOrigem, int contaDestino, double valor) throws SQLException {
        ContaCorrente origem = carregarConta(contaOrigem);
        ContaCorrente destino = carregarConta(contaDestino);

        if (origem != null && destino != null && origem.getSaldo() >= valor) {
            origem.debitar(valor);
            destino.creditar(valor);
            atualizarConta(origem);
            atualizarConta(destino);
            return true; // Transferência realizada com sucesso
        } else {
            return false; // Saldo insuficiente na conta de origem ou contas não encontradas
        }
    }

    // Método para obter a lista de contas correntes
    public List<ContaCorrente> getContas() throws SQLException {
        List<ContaCorrente> contas = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ContaCorrente")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    double saldo = resultSet.getDouble("saldo");
                    boolean ativa = resultSet.getBoolean("ativa");
                    contas.add(new ContaCorrente(id, saldo, ativa));
                }
            }
        }
        return contas;
    }

    // Método para carregar uma conta corrente do banco de dados
    private ContaCorrente carregarConta(int numeroConta) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM ContaCorrente WHERE id = ?")) {
            statement.setInt(1, numeroConta);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double saldo = resultSet.getDouble("saldo");
                    boolean ativa = resultSet.getBoolean("ativa");
                    return new ContaCorrente(numeroConta, saldo, ativa);
                }
            }
        }
        return null; // Conta não encontrada
    }

    // Método para atualizar uma conta corrente no banco de dados
    private void atualizarConta(ContaCorrente conta) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE ContaCorrente SET saldo = ?, ativa = ? WHERE id = ?")) {
            statement.setDouble(1, conta.getSaldo());
            statement.setBoolean(2, conta.isAtiva());
            statement.setInt(3, conta.getId());
            statement.executeUpdate();
        }
    }
}
