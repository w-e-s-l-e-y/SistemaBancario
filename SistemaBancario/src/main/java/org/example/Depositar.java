package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Depositar extends JFrame implements ActionListener {
    private JTextField campoConta;
    private JTextField campoValor;
    private JButton botaoConfirmar;

    public Depositar() {
        setTitle("Depósito");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        campoConta = new JTextField(20);
        campoValor = new JTextField(20);
        botaoConfirmar = new JButton("Confirmar");

        setLayout(new GridLayout(3, 1));
        add(new JLabel("Número da Conta:"));
        add(campoConta);
        add(new JLabel("Valor do Depósito:"));
        add(campoValor);
        add(botaoConfirmar);

        botaoConfirmar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botaoConfirmar) {
            // Aqui você implementará a lógica para realizar o depósito
            // Por enquanto, apenas exibiremos a mensagem de sucesso
            JOptionPane.showMessageDialog(this, "Depósito realizado com sucesso!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Depositar().setVisible(true);
            }
        });
    }
}
