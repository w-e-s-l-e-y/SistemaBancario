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
    private ContaCorrente conta; // Instância da classe Conta
    private JLabel lblSaldo;

    public InterfacePrincipal(ContaCorrente conta) {
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
                conta.depositar(valor); // Atualiza o saldo na classe ContaCorrente
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
                conta.sacar(valor); // Atualiza o saldo na classe ContaCorrente
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
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:E:\\SistemaBancario\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db")) {
            String sql = "UPDATE ContaCorrente SET saldo = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, conta.getSaldo());
                statement.setInt(2, conta.getId());
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
                ContaCorrente conta = new ContaCorrente(123, 1000.0, true); // Número da conta, saldo inicial e status da conta
                new InterfacePrincipal(conta).setVisible(true);
            }
        });
    }
}
