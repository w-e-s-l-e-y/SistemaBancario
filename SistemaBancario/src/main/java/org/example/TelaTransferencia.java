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

public class TelaTransferencia extends JFrame {
    private JTextField txtNumeroContaDestino;
    private JTextField txtValorTransferencia;
    private int numeroContaOrigem;

    public TelaTransferencia(int numeroContaOrigem) {
        this.numeroContaOrigem = numeroContaOrigem;

        setTitle("Transferência");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10)); // Layout do painel

        JLabel lblNumeroContaDestino = new JLabel("Número da Conta Destino:");
        txtNumeroContaDestino = new JTextField();
        JLabel lblValorTransferencia = new JLabel("Valor da Transferência:");
        txtValorTransferencia = new JTextField();
        JButton btnTransferir = new JButton("Transferir");
        btnTransferir.setBackground(new Color (135, 206, 250));

        // Adicionando componentes ao painel
        panel.add(lblNumeroContaDestino);
        panel.add(txtNumeroContaDestino);
        panel.add(lblValorTransferencia);
        panel.add(txtValorTransferencia);
        panel.add(new JLabel()); // Espaçamento vazio
        panel.add(btnTransferir);

        // Adicionando ação ao botão de transferência
        btnTransferir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarTransferencia();
            }
        });

        // Adicionando o painel à janela
        add(panel);
    }

    private void realizarTransferencia() {
        String numeroContaDestinoStr = txtNumeroContaDestino.getText();
        String valorTransferenciaStr = txtValorTransferencia.getText();

        if (!numeroContaDestinoStr.isEmpty() && !valorTransferenciaStr.isEmpty()) {

            try {
                int numeroContaDestino = Integer.parseInt(numeroContaDestinoStr);
                double valorTransferencia = Double.parseDouble(valorTransferenciaStr);

                // Verifica se a conta destino existe
                if (verificarExistenciaConta(numeroContaDestino)) {
                    // Realiza a transferência
                    transferir(numeroContaOrigem, numeroContaDestino, valorTransferencia);
                } else {
                    JOptionPane.showMessageDialog(this, "A conta destino não foi encontrada.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Número de conta ou valor de transferência inválido.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.");
        }
    }

    private boolean verificarExistenciaConta(int numeroConta) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\964610\\Documents\\GitHub\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db")) {
            String sql = "SELECT * FROM ContaCorrente WHERE cliente_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, numeroConta);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next(); // Retorna true se a conta existe, false caso contrário
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao verificar a existência da conta: " + ex.getMessage());
            return false;
        }
    }

    private void transferir(int numeroContaOrigem, int numeroContaDestino, double valorTransferencia) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\964610\\Documents\\GitHub\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db")) {
            // Verifica se há saldo suficiente na conta de origem
            String sqlSaldo = "SELECT saldo, cheque_especial FROM ContaCorrente WHERE cliente_id = ?";
            try (PreparedStatement statementSaldo = connection.prepareStatement(sqlSaldo)) {
                statementSaldo.setInt(1, numeroContaOrigem);
                ResultSet resultSetSaldo = statementSaldo.executeQuery();
                if (resultSetSaldo.next()) {
                    double saldoOrigem = resultSetSaldo.getDouble("saldo");
                    double chequeEspecialOrigem = resultSetSaldo.getDouble("cheque_especial");

                    // Verifica se o saldo disponível (saldo + cheque especial) é suficiente para a transferência
                    if ((saldoOrigem + chequeEspecialOrigem) >= valorTransferencia) {
                        // Saldo suficiente, realiza a transferência normalmente
                        double novoSaldoOrigem = saldoOrigem - valorTransferencia;
                        double diferenca = saldoOrigem-valorTransferencia;
                        double novoSaldo = saldoOrigem - (valorTransferencia+diferenca);

                        // Verifica se o novo saldo é negativo e utiliza o cheque especial se necessário
                        if (novoSaldoOrigem < 0) {
                            double valorUtilizadoChequeEspecial = Math.abs(novoSaldoOrigem);
                            double novoSaldoChequeEspecial = chequeEspecialOrigem - valorUtilizadoChequeEspecial;

                            // Atualiza o saldo na conta de origem e o cheque especial
                            String sqlTransferencia = "UPDATE ContaCorrente SET saldo = ?, cheque_especial = ? WHERE cliente_id = ?";
                            try (PreparedStatement statementTransferencia = connection.prepareStatement(sqlTransferencia)) {
                                statementTransferencia.setDouble(1, novoSaldo); // Saldo fica em zero
                                statementTransferencia.setDouble(2, novoSaldoChequeEspecial); // Atualiza o cheque especial
                                statementTransferencia.setInt(3, numeroContaOrigem);
                                statementTransferencia.executeUpdate();
                            }

                            JOptionPane.showMessageDialog(this, "Transferência de R$ " + valorTransferencia + " realizada utilizando o cheque especial.");
                        } else {
                            // Atualiza o saldo na conta de origem
                            String sqlTransferencia = "UPDATE ContaCorrente SET saldo = ? WHERE cliente_id = ?";
                            try (PreparedStatement statementTransferencia = connection.prepareStatement(sqlTransferencia)) {
                                statementTransferencia.setDouble(1, novoSaldo);
                                statementTransferencia.setInt(2, numeroContaOrigem);
                                statementTransferencia.executeUpdate();
                            }

                            JOptionPane.showMessageDialog(this, "Transferência de R$ " + valorTransferencia + " realizada com sucesso.");
                        }


                        // Atualiza o saldo na conta de destino
                        String sqlTransferencia = "UPDATE ContaCorrente SET saldo = saldo + ? WHERE cliente_id = ?";
                        try (PreparedStatement statementTransferencia = connection.prepareStatement(sqlTransferencia)) {
                            statementTransferencia.setDouble(1, valorTransferencia);
                            statementTransferencia.setInt(2, numeroContaDestino);
                            statementTransferencia.executeUpdate();
                        }

                        dispose(); // Fecha a janela de transferência
                    } else {
                        JOptionPane.showMessageDialog(this, "Saldo insuficiente na conta de origem.");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao realizar a transferência: " + ex.getMessage());
        }
    }




    public double getNovoSaldo() {
        double novoSaldo = 0.0;

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\964610\\Documents\\GitHub\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db")) {
            String sql = "SELECT saldo FROM ContaCorrente WHERE cliente_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, numeroContaOrigem);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    novoSaldo = resultSet.getDouble("saldo");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao obter o novo saldo: " + ex.getMessage());
        }

        return novoSaldo;
    }

    public double getNovoChequeEspecial() {
        // Aqui você deve implementar a lógica para obter o novo valor do cheque especial
        // Supondo que você tenha um objeto de ContaCorrente chamado "conta" para acessar os métodos de instância

        double saldoAtual = this.getNovoSaldo(); // Obtém o saldo atual da conta
        double chequeEspecialAtual = this.getChequeEspecial(); // Obtém o cheque especial atual da conta

        // Verifica se o saldo atual é menor ou igual a zero
        if (saldoAtual <= 0) {
            // Se o saldo for menor ou igual a zero, o cheque especial será o valor absoluto do saldo atual
            return Math.abs(saldoAtual);
        } else {
            // Se o saldo for positivo, retorna o cheque especial atual
            return chequeEspecialAtual;
        }
    }

    public double getChequeEspecial() {
        // Lógica para obter o cheque especial da conta
        // Você deve substituir esta implementação pela forma correta de obter o cheque especial da conta
        return 0.0; // Implementação temporária
    }
}
