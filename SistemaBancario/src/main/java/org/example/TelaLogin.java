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
import java.util.ArrayList;
import java.util.List;

public class TelaLogin extends JFrame implements ActionListener {
    private JTextField campoNome;
    private JTextField campoNumeroConta;
    private JButton btnConfirmar;
    private boolean loginSuccessful = false;
    private List<ActionListener> loginListeners = new ArrayList<>();

    public TelaLogin() {
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        campoNome = new JTextField(20);
        campoNumeroConta = new JTextField(20);
        btnConfirmar = new JButton("Confirmar");

        setLayout(new GridLayout(3, 1));
        add(new JLabel("Nome:"));
        add(campoNome);
        add(new JLabel("Número da Conta:"));
        add(campoNumeroConta);
        add(btnConfirmar);

        btnConfirmar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConfirmar) {
            String nome = campoNome.getText();
            int numeroConta = Integer.parseInt(campoNumeroConta.getText());

            // Verificar se o nome e o número da conta correspondem no banco de dados
            if (validarLogin(nome, numeroConta)) {
                JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
                loginSuccessful = true; // Login bem-sucedido
                notificarListeners();
                dispose(); // Fecha a tela de login após o login bem-sucedido
            } else {
                JOptionPane.showMessageDialog(this, "Nome ou número da conta inválidos.");
            }
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public void addLoginListener(ActionListener listener) {
        loginListeners.add(listener);
    }

    private void notificarListeners() {
        for (ActionListener listener : loginListeners) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    private boolean validarLogin(String nome, int numeroConta) {
        String url = "jdbc:sqlite:C:\\Users\\964610\\Documents\\GitHub\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db";
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT COUNT(*) FROM Cliente WHERE nome = ? AND id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nome);
                statement.setInt(2, numeroConta);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next() && resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }
}
