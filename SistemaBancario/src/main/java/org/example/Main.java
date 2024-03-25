package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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

        // Criar tabelas se elas não existirem no banco de dados
        criarTabelasSeNecessario();
    }

    private static void criarTabelasSeNecessario() {
        // Estabelecer a conexão com o banco de dados (substitua os valores conforme necessário)
        String url = "jdbc:mysql://localhost:3306/wykbank"; // URL atualizada para "wykbank"
        String username = "seu_usuario";
        String password = "sua_senha";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            // Criar as tabelas se elas não existirem
            criarTabelaCliente(connection);
            criarTabelaContaCorrente(connection);

            System.out.println("Tabelas criadas com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar as tabelas: " + e.getMessage());
        }
    }

    private static void criarTabelaCliente(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Cliente (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "nome VARCHAR(100) NOT NULL," +
                "idade INTEGER NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "tipo INTEGER NOT NULL," +
                "ativo BOOLEAN NOT NULL" +
                ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private static void criarTabelaContaCorrente(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS ContaCorrente (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
                "saldo DECIMAL(10, 2) NOT NULL," +
                "ativa BOOLEAN NOT NULL," +
                "cliente_id INTEGER NOT NULL," +
                "FOREIGN KEY (cliente_id) REFERENCES Cliente(id)" +
                ")";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
}
