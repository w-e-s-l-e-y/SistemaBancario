package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfacePrincipal extends JFrame {



    private JLabel lblSaldo;
    private double chequeEspecialInicial;
    private RealtimeDatabase database;
    private ContaCorrente conta;
    private RealtimeDatabase realtimeDatabase;


    public InterfacePrincipal(ContaCorrente conta, RealtimeDatabase database) {
        this.conta = conta;
        this.database = database;

        chequeEspecialInicial = conta.getChequeEspecial();

        setTitle("Sistema Bancário");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        lblSaldo = new JLabel("Saldo atual: R$ " + conta.getSaldo());
        JButton btnDeposito = new JButton("Depósito");
        JButton btnSaque = new JButton("Saque");
        JButton btnTransferir = new JButton("Transferir");
        JButton btnLogSaldoChequeEspecial = new JButton("Log Saldo e Cheque Especial");

        lblSaldo.setFont(new Font("Arial", Font.BOLD, 16));
        btnDeposito.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSaque.setFont(new Font("Arial", Font.PLAIN, 14));
        btnTransferir.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLogSaldoChequeEspecial.setFont(new Font("Arial", Font.PLAIN, 14));
        btnDeposito.setBackground(new Color(152, 251, 152));
        btnSaque.setBackground(new Color(255, 182, 193));
        btnTransferir.setBackground(new Color(135, 206, 250));
        btnLogSaldoChequeEspecial.setBackground(new Color(240, 230, 140));

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

        btnTransferir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaTransferencia();
            }
        });

        btnLogSaldoChequeEspecial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logSaldoEChequeEspecial();
            }
        });

        lblSaldo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(lblSaldo);
        panel.add(btnDeposito);
        panel.add(btnSaque);
        panel.add(btnTransferir);
        panel.add(btnLogSaldoChequeEspecial);

        add(panel);
    }

    private void realizarDeposito() {
        String valorStr = JOptionPane.showInputDialog("Digite o valor do depósito:");
        if (valorStr != null && !valorStr.isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr);
                conta.depositar(valor);
                atualizarFirebase(conta.getNumeroConta());
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
                conta.sacar(valor);
                atualizarFirebase(conta.getNumeroConta());
                lblSaldo.setText("Saldo atual: R$ " + conta.getSaldo());
                JOptionPane.showMessageDialog(this, "Saque de R$ " + valor + " realizado com sucesso.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void abrirTelaTransferencia() {
        TelaTransferencia telaTransferencia = new TelaTransferencia(conta.getNumeroConta());
        telaTransferencia.setVisible(true);
        telaTransferencia.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                try {
                    ContaCorrente contaAtualizada = database.getContaCorrente(conta.getNumeroConta());
                    if (contaAtualizada != null) {
                        conta = contaAtualizada;
                        lblSaldo.setText("Saldo atual: R$ " + conta.getSaldo());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void atualizarFirebase(int numeroConta) {
        database.setValue("correntistas/" + numeroConta + "/saldo", conta.getSaldo());
        database.setValue("correntistas/" + numeroConta + "/cheque_Especial", conta.getChequeEspecial());
    }

    private void logSaldoEChequeEspecial() {
        System.out.println("Botão Log Saldo e Cheque Especial clicado"); // Adiciona mensagem de depuração
        // Executa a operação em uma thread separada
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Iniciando busca do saldo e cheque especial"); // Adiciona mensagem de depuração
                    ContaCorrente contaAtualizada = database.getContaCorrente(conta.getNumeroConta());
                    if (contaAtualizada != null) {
                        System.out.println("Dados da conta encontrados"); // Adiciona mensagem de depuração
                        System.out.println("Saldo: R$ " + contaAtualizada.getSaldo());
                        System.out.println("Cheque Especial: R$ " + contaAtualizada.getChequeEspecial());
                    } else {
                        System.out.println("Erro ao obter dados da conta.");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void atualizarSaldo(double novoSaldo) {
        this.conta.setSaldo(novoSaldo);
        lblSaldo.setText("Saldo atual: R$ " + novoSaldo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RealtimeDatabase database = new RealtimeDatabase();
                try {
                    ContaCorrente conta = database.getContaCorrente(123);
                    if (conta != null) {
                        new InterfacePrincipal(conta, database).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Conta não encontrada.");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
