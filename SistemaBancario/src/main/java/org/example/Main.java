package org.example;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties properties = new Properties();
        String dbPath = null; // Definir dbPath fora do bloco try-catch

        try {
            // Carregar o arquivo de propriedades
            properties.load(Main.class.getResourceAsStream("/config.properties"));



            // Obter o valor da propriedade 'db.path'
            dbPath = properties.getProperty("db.path");

            // Exibir o caminho do banco de dados
            System.out.println("O caminho do banco de dados definido é: " + dbPath);

            // Criar o banco de dados se ele não existir
            criarBancoSeNecessario(dbPath);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar o arquivo de configuração.");
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Criar e exibir a tela de início
                new TelaInicio().setVisible(true);
            }
        });
    }

    private static void criarBancoSeNecessario(String dbPath) {
        // Verificar se o banco de dados já existe
        File dbFile = new File(dbPath);
        if (!dbFile.exists()) {
            // Se o banco de dados não existir, criar o arquivo
            try {
                if (dbFile.createNewFile()) {
                    System.out.println("Banco de dados criado com sucesso em: " + dbPath);
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
        String url = "jdbc:sqlite:" + dbPath;

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

    public static String obterCaminhoBancoDados() {
        Properties properties = new Properties();
        try {
            // Carregar o arquivo de propriedades do diretório de recursos
            properties.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));

            // Obter o valor da propriedade 'db.path'
            String dbPath = properties.getProperty("db.path");

            // Log do caminho do banco de dados
            System.out.println("Caminho do banco de dados obtido: " + dbPath);

            return properties.getProperty("db.path");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar o arquivo de configuração.");
            return null;
        }
    }

}
