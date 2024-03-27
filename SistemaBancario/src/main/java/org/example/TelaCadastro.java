package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TelaCadastro extends JFrame implements ActionListener {
    private JTextField campoNome;
    private JTextField campoIdade;
    private JTextField campoEmail;
    private JTextField campoTipo;
    private JButton btnCadastrar;

    public TelaCadastro() {
        setTitle("Cadastro de Cliente");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        campoNome = new JTextField(20);
        campoIdade = new JTextField(20);
        campoEmail = new JTextField(20);
        campoTipo = new JTextField(20);
        btnCadastrar = new JButton("Cadastrar");

        setLayout(new GridLayout(5, 1));
        add(new JLabel("Nome:"));
        add(campoNome);
        add(new JLabel("Idade:"));
        add(campoIdade);
        add(new JLabel("Email:"));
        add(campoEmail);
        add(new JLabel("Tipo:"));
        add(campoTipo);
        add(btnCadastrar);

        btnCadastrar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCadastrar) {
            // Obter os dados dos campos de texto
            String nome = campoNome.getText();
            int idade = Integer.parseInt(campoIdade.getText());
            String email = campoEmail.getText();
            int tipo = Integer.parseInt(campoTipo.getText());

            // Estabelecer a conexão com o banco de dados SQLite
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\964610\\Documents\\GitHub\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db")) {
                // Inserir o cliente na tabela Cliente
                String sqlCliente = "INSERT INTO Cliente (nome, idade, email, tipo, ativo) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sqlCliente, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, nome);
                    statement.setInt(2, idade);
                    statement.setString(3, email);
                    statement.setInt(4, tipo);
                    statement.setBoolean(5, true); // Supondo que o cliente é sempre cadastrado como ativo

                    // Executar a instrução SQL para inserir o cliente
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        // Recuperar o id do cliente recém-inserido
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int clienteId = generatedKeys.getInt(1);
                            // Inserir uma nova conta associada ao cliente na tabela Conta
                            String sqlConta = "INSERT INTO ContaCorrente (saldo, ativa, cliente_id) VALUES (?, ?, ?)";
                            try (PreparedStatement contaStatement = connection.prepareStatement(sqlConta)) {
                                contaStatement.setDouble(1, 100); // Saldo inicial 0
                                contaStatement.setBoolean(2, true); // Conta ativa
                                contaStatement.setInt(3, clienteId); // Id do cliente
                                contaStatement.executeUpdate();
                            }
                        }
                        JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
                        this.dispose(); // Fechar a tela de cadastro
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente.");
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastro().setVisible(true);
            }
        });
    }
}
