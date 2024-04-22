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
        JButton btnTransferir = new JButton("Transferir");

        // Estilização dos componentes
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 16));
        btnDeposito.setFont(new Font("Arial", Font.PLAIN, 14));
        btnSaque.setFont(new Font("Arial", Font.PLAIN, 14));
        btnTransferir.setFont(new Font("Arial", Font.PLAIN, 14));
        btnDeposito.setBackground(new Color(152, 251, 152)); // Verde claro
        btnSaque.setBackground(new Color(255, 182, 193)); // Rosa claro
        btnTransferir.setBackground(new Color(135, 206, 250)); // Azul claro

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

        btnTransferir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaTransferencia();
            }
        });

        // Estilizando borda
        lblSaldo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Adicionando componentes ao painel principal com espaçamento
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10)); // Espaçamento entre os componentes
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Espaçamento das bordas
        panel.add(lblSaldo);
        panel.add(btnDeposito);
        panel.add(btnSaque);
        panel.add(btnTransferir);

        // Adicionando o painel principal à janela
        add(panel);
    }

    private void realizarDeposito() {
        String valorStr = JOptionPane.showInputDialog("Digite o valor do depósito:");
        if (valorStr != null && !valorStr.isEmpty()) {
            try {
                double valor = Double.parseDouble(valorStr);

                double saldoAnterior = conta.getSaldo();
                double saldoPosterior = saldoAnterior + valor;

                double novoChequeEspecial = conta.getChequeEspecial();

                if (saldoPosterior >= 0) {
                    // Se o saldo após o depósito for maior ou igual a zero, ajusta o cheque especial
                    // para 1/3 do saldo após o depósito e mantém no máximo o valor atual do cheque especial
                    novoChequeEspecial = Math.min(saldoPosterior / 3, novoChequeEspecial);
                }


                double valorDeposito = valor;
                if (saldoPosterior < 0) {
                    // Se o saldo após o depósito for menor que zero, ajuste o valor do depósito
                    // para cobrir o saldo negativo
                    valorDeposito = valor - saldoAnterior;
                }

                conta.depositar(valorDeposito); // Atualiza o saldo na classe ContaCorrente
                double novoSaldo = conta.getSaldo(); // Obtém o novo saldo

                // Atualiza o saldo e o cheque especial no banco de dados
                atualizarSaldoBancoDados(conta.getNumeroConta(), novoSaldo, novoChequeEspecial);

                lblSaldo.setText("Saldo atual: R$ " + novoSaldo);
                JOptionPane.showMessageDialog(this, "Depósito de R$ " + valor + " realizado com sucesso.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor inválido.");
            }
        }
    }

    private void realizarSaque() {
        String valorStr = JOptionPane.showInputDialog("Digite o valor do saque:");
        if (valorStr != null && !valorStr.isEmpty()) {
            double valor = Double.parseDouble(valorStr);
            // Verifica se o valor do saque é positivo
            if (valor <= 0) {
                JOptionPane.showMessageDialog(this, "Valor inválido para saque.");
                return;
            }

            // Calcula o saldo após o saque
            double saldoAtual = conta.getSaldo();
            double novoSaldo = saldoAtual - valor;

            // Verifica se o saldo após o saque é suficiente
            if (novoSaldo >= 0) {
                // O saldo é suficiente, apenas atualiza o saldo na conta e no banco de dados
                conta.sacar(valor);
                atualizarSaldoBancoDados(conta.getNumeroConta(), conta.getSaldo(), conta.getChequeEspecial());
                lblSaldo.setText("Saldo atual: R$ " + conta.getSaldo());
                JOptionPane.showMessageDialog(this, "Saque de R$ " + valor + " realizado com sucesso.");
            } else {
                // O saldo não é suficiente, utiliza o cheque especial se disponível
                double saldoRestante = Math.abs(novoSaldo);
                if (saldoRestante > conta.getChequeEspecial()) {
                    JOptionPane.showMessageDialog(this, "Saldo insuficiente.");
                    return;
                }
                // Atualiza o saldo na conta e no banco de dados
                double valorSaque = valor - saldoAtual; // Valor a ser sacado do cheque especial
                conta.sacar(valorSaque);
                double novoChequeEspecial = conta.getChequeEspecial() - valorSaque;
                // Atualiza o saldo no banco de dados
                atualizarSaldoBancoDados(conta.getNumeroConta(), conta.getSaldo(), novoChequeEspecial);
                lblSaldo.setText("Saldo atual: R$ " + conta.getSaldo());
                JOptionPane.showMessageDialog(this, "Saque de R$ " + valor + " realizado com sucesso.");
            }
        }
    }




    private void abrirTelaTransferencia() {
        TelaTransferencia telaTransferencia = new TelaTransferencia(conta.getNumeroConta());
        telaTransferencia.setVisible(true);

        // Passa a conta, o saldo atual e o cheque especial para a tela de transferência
        telaTransferencia.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                double novoSaldo = telaTransferencia.getNovoSaldo();
                double novoChequeEspecial = telaTransferencia.getNovoChequeEspecial(); // Obtém o novo valor do cheque especial
                atualizarSaldoBancoDados(conta.getNumeroConta(), novoSaldo, novoChequeEspecial); // Atualiza o saldo e o cheque especial no banco de dados
                lblSaldo.setText("Saldo atual: R$ " + novoSaldo);
            }
        });
    }



    public static void atualizarSaldoBancoDados(int numeroContaOrigem, double novoSaldo, double novoChequeEspecial) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\964610\\Documents\\GitHub\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db")) {
            String sql = "UPDATE ContaCorrente SET saldo = ?, cheque_especial = ? WHERE cliente_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDouble(1, novoSaldo);
                statement.setDouble(2, novoChequeEspecial);
                statement.setInt(3, numeroContaOrigem);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atualizar saldo no banco de dados: " + ex.getMessage());
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
