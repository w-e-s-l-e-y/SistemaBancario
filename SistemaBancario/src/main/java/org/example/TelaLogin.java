package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaLogin extends JFrame implements ActionListener {
    private JTextField campoNome;
    private JTextField campoNumeroConta;
    private JButton btnConfirmar;

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

            // Aqui você pode implementar a lógica para validar o login com o banco de dados
            // Por enquanto, apenas exibiremos uma mensagem com os dados inseridos
            JOptionPane.showMessageDialog(this, "Nome: " + nome + "\nNúmero da Conta: " + numeroConta);
        }
    }
}