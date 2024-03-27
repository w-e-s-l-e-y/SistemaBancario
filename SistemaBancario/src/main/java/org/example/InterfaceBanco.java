package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceBanco extends JFrame {
    private JButton btnLogin;
    private JButton btnCadastro;
    private JButton btnSaldo;
    private JButton btnDeposito;
    private JButton btnTransferencia;
    private JButton btnSaque;

    private double saldo = 1000.00; // Exemplo de saldo inicial

    public InterfaceBanco() {
        // Configurações básicas da janela
        setTitle("Sistema Bancário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Inicialização dos botões
        btnLogin = new JButton("Login");
        btnCadastro = new JButton("Cadastro/Login");
        btnSaldo = new JButton("Saldo");
        btnDeposito = new JButton("Depósito");
        btnTransferencia = new JButton("Transferência");
        btnSaque = new JButton("Saque");

        // Adicionando ações aos botões
        btnSaldo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSaldo();
            }
        });

        btnDeposito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDeposito();
            }
        });

        btnSaque.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarSaque();
            }
        });

        btnTransferencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implemente sua lógica para realizar a transferência
                realizarTransferencia();
                JOptionPane.showMessageDialog(null, "Transferência realizada com sucesso.");
            }
        });

        // Adicionando botões ao painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3));
        panel.add(btnLogin);
        panel.add(btnCadastro);
        panel.add(btnSaldo);
        panel.add(btnDeposito);
        panel.add(btnTransferencia);
        panel.add(btnSaque);

        // Adicionando o painel principal à janela
        add(panel);
    }

    private void realizarTransferencia() {
        // Implemente sua lógica para realizar a transferência
        String[] opcoes = {"Conta Corrente", "Conta Poupança"};
        JComboBox<String> comboBox = new JComboBox<>(opcoes);
        JTextField txtValor = new JTextField();
        JTextField txtConta = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Conta:"));
        panel.add(comboBox);
        panel.add(new JLabel("Valor:"));
        panel.add(txtValor);
        panel.add(new JLabel("Conta destino:"));
        panel.add(txtConta);
        int result = JOptionPane.showConfirmDialog(null, panel, "Transferência",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Implemente sua lógica para realizar a transferência
            String conta = (String) comboBox.getSelectedItem();
            double valor = Double.parseDouble(txtValor.getText());
            // Implemente sua lógica para realizar a transferência
            if (conta.equals("Conta Corrente") && saldo >= valor) {
                saldo -= valor;
                JOptionPane.showMessageDialog(this, "Transferência de R$ " + valor + " realizada com sucesso.");
            } else if (conta.equals("Conta Poupança")) {
                JOptionPane.showMessageDialog(this, "Transferência de R$ " + valor + " realizada com sucesso.");

            }
            }
        }


    private void mostrarSaldo() {
        JOptionPane.showMessageDialog(this, "Saldo atual: R$ " + saldo);
    }

    private void mostrarDeposito() {
        JTextField txtValor = new JTextField();
        JTextField txtConta = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Valor a depositar:"));
        panel.add(txtValor);
        panel.add(new JLabel("Número da conta:"));
        panel.add(txtConta);
        int result = JOptionPane.showConfirmDialog(null, panel, "Depósito",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            double valor = Double.parseDouble(txtValor.getText());
            // Implemente sua lógica para depositar o valor
            JOptionPane.showMessageDialog(this, "Depósito de R$ " + valor + " realizado com sucesso.");
        }
    }

    private void mostrarSaque() {
        JTextField txtValor = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Valor a sacar:"));
        panel.add(txtValor);
        int result = JOptionPane.showConfirmDialog(null, panel, "Saque",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            double valor = Double.parseDouble(txtValor.getText());
            if (valor > saldo) {
                JOptionPane.showMessageDialog(this, "Saldo insuficiente.");
            } else {
                saldo -= valor;
                JOptionPane.showMessageDialog(this, "Saque de R$ " + valor + " realizado com sucesso.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new InterfaceBanco().setVisible(true);
            }
        });
    }
}
