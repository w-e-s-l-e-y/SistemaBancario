package org.example;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Criar o banco de dados se ele não existir
                criarBancoSeNecessario();

                // Criar e exibir a tela de início
                new TelaInicio().setVisible(true);
            }
        });
    }

    private static void criarBancoSeNecessario() {
        // Definir o caminho para o banco de dados SQLite
        String path = "C:\\Users\\964610\\Documents\\GitHub\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db"; // Substitua pelo caminho desejado

        // Verificar se o banco de dados já existe
        File dbFile = new File(path);
        if (!dbFile.exists()) {
            // Se o banco de dados não existir, criar o arquivo
            try {
                if (dbFile.createNewFile()) {
                    System.out.println("Banco de dados criado com sucesso em: " + path);
                } else {
                    System.out.println("Falha ao criar o banco de dados.");
                    return;
                }
            } catch (Exception e) {
                System.err.println("Erro ao criar o banco de dados: " + e.getMessage());
                return;
            }
        }

        // Estabelecer a conexão com o banco de dados SQLite
        String url = "jdbc:sqlite:" + path;

        try (Connection connection = DriverManager.getConnection(url)) {
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
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(100) NOT NULL," +
                "idade INTEGER NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "tipo INTEGER NOT NULL," +
                "ativo BOOLEAN NOT NULL" +
                ")";

        try (java.sql.Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private static void criarTabelaContaCorrente(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS ContaCorrente (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "saldo DECIMAL(10, 2) NOT NULL," +
                "ativa BOOLEAN NOT NULL," +
                "cliente_id INTEGER NOT NULL," +
                "FOREIGN KEY (cliente_id) REFERENCES Cliente(id)" +
                ")";

        try (java.sql.Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
}
