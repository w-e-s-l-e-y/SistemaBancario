package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cliente {
    private int id;
    private String nome;
    private int idade;
    private String email;
    private int tipo;
    private boolean ativo;

    public Cliente(int id, String nome, int idade, String email, int tipo, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.email = email;
        this.tipo = tipo;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if (idade >= 0) {
            this.idade = idade;
        } else {
            throw new IllegalArgumentException("A idade deve ser um valor não negativo.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.contains("@") && email.contains(".")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("O e-mail fornecido não é válido.");
        }
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void salvarNoBanco(Connection connection) throws SQLException {
        String sql = "INSERT INTO Cliente (id, nome, idade, email, tipo, ativo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.setString(2, nome);
            statement.setInt(3, idade);
            statement.setString(4, email);
            statement.setInt(5, tipo);
            statement.setBoolean(6, ativo);
            statement.executeUpdate();
        }
    }

    public static Cliente carregarDoBanco(int id, Connection connection) throws SQLException {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String nome = resultSet.getString("nome");
                    int idade = resultSet.getInt("idade");
                    String email = resultSet.getString("email");
                    int tipo = resultSet.getInt("tipo");
                    boolean ativo = resultSet.getBoolean("ativo");
                    return new Cliente(id, nome, idade, email, tipo, ativo);
                }
            }
        }
        return null;
    }
}
