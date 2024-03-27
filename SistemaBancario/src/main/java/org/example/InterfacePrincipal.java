package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InterfacePrincipal extends JFrame {
    private Conta conta; // Instância da classe Conta
    private JLabel lblSaldo;

    public InterfacePrincipal() {
        this.conta = conta;

        // Configurações básicas da janela
        setTitle("Sistema Bancário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Inicialização dos componentes
        lblSaldo = new JLabel("Saldo atual: R$ " + conta.getSaldo());
        JButton btnDeposito = new JButton("Depósito");
        JButton btnSaque = new JButton("Saque");

        // Adicionando ações aos botões
        btnDeposito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarDeposito();
            }
        });

        btnSaque.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarSaque();
            }
        });

        // Adicionando componentes ao painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1));
        panel.add(lblSaldo);
        panel.add(btnDeposito);
        panel.add(btnSaque);

        // Adicionando o painel principal à janela
        add(panel);
    }

    private void realizarDeposito() {
        String valorStr = JOptionPane.showInputDialog("Digite o valor do depósito:");
        if (valorStr != null && !valorStr.isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr);
                conta.creditar(valor); // Atualiza o saldo na classe Conta
                atualizarSaldoBancoDados(); // Atualiza o saldo no banco de dados
                lblSaldo.setText("Saldo atual: R$ " + conta.getSaldo());
                JOptionPane.showMessageDialog(this, "Depósito de R$ " + valor + " realizado com sucesso.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido.");
            }
        }
    }

    private void realizarSaque() {
        String valorStr = JOptionPane.showInputDialog("Digite o valor do saque:");
        if (valorStr != null && !valorStr.isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr);
                conta.debitar(valor); // Atualiza o saldo na classe Conta
                atualizarSaldoBancoDados(); // Atualiza o saldo no banco de dados
                lblSaldo.setText("Saldo atual: R$ " + conta.getSaldo());
                JOptionPane.showMessageDialog(this, "Saque de R$ " + valor + " realizado com sucesso.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void atualizarSaldoBancoDados() {
        // Atualiza o saldo no banco de dados
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:seu_banco_de_dados.db")) {
            String sql = "UPDATE Conta SET saldo = ? WHERE numero = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, conta.getSaldo());
                statement.setInt(2, conta.getNumero());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar saldo no banco de dados: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Exemplo de uso da InterfacePrincipal com uma Conta
                Conta conta = new Conta(123, 1000.0); // Número da conta e saldo inicial
                new InterfacePrincipal().setVisible(true);
            }
        });
    }
}
