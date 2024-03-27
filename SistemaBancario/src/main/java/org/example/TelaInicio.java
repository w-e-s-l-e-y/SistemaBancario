package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TelaInicio extends JFrame implements ActionListener {
    private JButton btnLogin;
    private JButton btnCadastro;

    public TelaInicio() {
        setTitle("Tela Inicial");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnLogin = new JButton("Login");
        btnCadastro = new JButton("Cadastro");

        setLayout(new GridLayout(2, 1));
        add(btnLogin);
        add(btnCadastro);

        btnLogin.addActionListener(this);
        btnCadastro.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            abrirTelaLogin();
        } else if (e.getSource() == btnCadastro) {
            abrirTelaCadastro();
        }
    }

    private void abrirTelaLogin() {
        TelaLogin telaLogin = new TelaLogin();
        telaLogin.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (telaLogin.isLoginSuccessful()) {
                    fecharTelaInicio(); // Fechar a tela de início
                    abrirInterfacePrincipal();
                    telaLogin.dispose(); // Fecha a tela de login após o login bem-sucedido
                }
            }
        });
        telaLogin.setVisible(true);
    }

    private void fecharTelaInicio() {
        dispose(); // Fecha a tela de início
    }

    private void abrirInterfacePrincipal() {
        InterfacePrincipal interfacePrincipal = new InterfacePrincipal();
        interfacePrincipal.setVisible(true);
    }

    private void abrirTelaCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro();
        telaCadastro.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaInicio().setVisible(true);
            }
        });
    }
}
