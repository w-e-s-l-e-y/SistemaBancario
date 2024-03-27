package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
                // Criar a instrução SQL para inserir um novo cliente
                String sql = "INSERT INTO Cliente (nome, idade, email, tipo, ativo) VALUES (?, ?, ?, ?, ?)";

                // Preparar a declaração SQL
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    // Definir os parâmetros da declaração SQL
                    statement.setString(1, nome);
                    statement.setInt(2, idade);
                    statement.setString(3, email);
                    statement.setInt(4, tipo);
                    statement.setBoolean(5, true); // Supondo que o cliente é sempre cadastrado como ativo

                    // Executar a instrução SQL
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!");
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