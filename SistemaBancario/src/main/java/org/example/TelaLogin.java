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
            String numeroContaStr = campoNumeroConta.getText();
            if (!numeroContaStr.isEmpty()) {
                int numeroConta = Integer.parseInt(numeroContaStr);
                if (validarNumeroConta(numeroConta)) {
                    // Se o número da conta for válido, define o login como bem-sucedido
                    loginSuccessful = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Número da conta inválido.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Digite um número de conta.");
            }
            notificarListeners();
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public String getNome() {
        return campoNome.getText();
    }

    public int getNumeroConta() {
        String numeroContaStr = campoNumeroConta.getText();
        if (!numeroContaStr.isEmpty()) {
            return Integer.parseInt(numeroContaStr);
        } else {
            return -1; // Retorna -1 se o campo estiver vazio
        }
    }

    public void addLoginListener(ActionListener listener) {
        loginListeners.add(listener);
    }

    private void notificarListeners() {
        for (ActionListener listener : loginListeners) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    // Método para validar o número da conta (você pode adicionar suas próprias regras de validação)
    private boolean validarNumeroConta(int numeroConta) {
        return numeroConta > 0; // Número da conta deve ser positivo
    }
}
