package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConsultaSaldo extends JFrame implements ActionListener {
    private JLabel labelConta;
    private JLabel labelSaldo;
    private JTextField campoConta, campoSaldo;
    private JButton botaoConsultar;

    private JButton botaoDepositar;

    public ConsultaSaldo(int numeroConta) {
        // Configurações da janela
        setTitle("Consulta de Saldo");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        // Criação dos componentes
        labelConta = new JLabel("Número da Conta:");
        labelSaldo = new JLabel("Saldo:");
        campoConta = new JTextField(20);
        campoSaldo = new JTextField(20);
        botaoConsultar = new JButton("Consultar");
        botaoDepositar = new JButton("Depositar");

        // Configuração do layout

        // Adiciona os componentes na tela

        add(labelConta);
        add(campoConta);
        add(labelSaldo);
        add(campoSaldo);
        add(botaoConsultar);
        add (botaoDepositar);

        // Associa o botão ao evento de clique
        botaoConsultar.addActionListener(this);
        botaoDepositar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Depositar depositar = new Depositar();
                depositar.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Ação do botão consultar
        if (e.getSource() == botaoConsultar) {
            // Aqui você pode incluir a lógica para consultar o saldo no banco de dados com base no número da conta
            // Por enquanto, apenas exibimos o número da conta e um saldo fixo como exemplo
            String conta = campoConta.getText();
            double saldo = consultarSaldo(conta); // Função fictícia para consultar o saldo
            campoSaldo.setText(String.valueOf(saldo));
        }
    }

    // Método fictício para consultar o saldo no banco de dados
    private double consultarSaldo(String conta) {
        // Aqui você deve implementar a lógica para consultar o saldo no banco de dados com base no número da conta
        // Por enquanto, apenas retornamos um saldo fixo como exemplo
        // Substitua isso pela sua lógica real de consulta ao banco de dados
        // Você precisará usar JDBC para se conectar ao banco de dados e executar a consulta
        // Retornaremos um saldo fictício para fins de demonstração
        return 1000.00; // Saldo fictício
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int numeroConta = 0;
                new ConsultaSaldo(numeroConta).setVisible(true);
            }
        });
    }
}
