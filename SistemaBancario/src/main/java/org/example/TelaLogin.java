package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TelaLogin extends JFrame implements ActionListener {
    private JTextField campoNome;
    private JTextField campoNumeroConta;
    private JButton btnConfirmar;
    private JLabel lblStatus;
    private boolean loginSuccessful = false;
    private List<ActionListener> loginListeners = new ArrayList<>();
    private RealtimeDatabase rtdb;

    public TelaLogin() {
        setTitle("Login");
        setSize(300, 250); // Aumenta o tamanho da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Adiciona uma margem ao redor do painel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Adiciona margens de 20px em todos os lados

        // Define um painel para os campos de entrada com GridBagLayout
        JPanel panelCampos = new JPanel(new GridBagLayout()); // Layout gerenciado pelo GridBagLayout para controle mais preciso

        // Cria um GridBagConstraints para configurar o posicionamento dos componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Adiciona margens de 5px em todos os lados

        // Adiciona rótulos e campos de entrada ao painel de campos
        JLabel labelNome = new JLabel("Nome:");
        campoNome = new JTextField(20);
        campoNome.setColumns(10); // Define a largura do campo
        campoNome.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Adiciona uma borda preta ao redor do campo
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCampos.add(labelNome, gbc);

        gbc.gridx = 1;
        panelCampos.add(campoNome, gbc);

        JLabel labelNumeroConta = new JLabel("Número da Conta:");
        campoNumeroConta = new JTextField(20);
        campoNumeroConta.setColumns(10); // Define a largura do campo
        campoNumeroConta.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Adiciona uma borda preta ao redor do campo
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelCampos.add(labelNumeroConta, gbc);

        gbc.gridx = 1;
        panelCampos.add(campoNumeroConta, gbc);

        // Adiciona o painel de campos ao painel principal
        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        // Cria um painel para o botão de confirmação e adiciona ao painel principal
        JPanel panelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setBackground(new Color(135, 206, 250));
        btnConfirmar.addActionListener(this);
        panelBotao.add(btnConfirmar);
        panelPrincipal.add(panelBotao, BorderLayout.SOUTH);

        // Cria um JLabel para exibir o status do login
        lblStatus = new JLabel();
        panelPrincipal.add(lblStatus, BorderLayout.NORTH);

        // Adiciona o painel principal à janela
        add(panelPrincipal);

        // Inicializa o RealtimeDatabase
        rtdb = new RealtimeDatabase();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnConfirmar) {
            String nome = campoNome.getText();
            String numeroContaStr = campoNumeroConta.getText();
            if (!numeroContaStr.isEmpty()) {
                int numeroConta = Integer.parseInt(numeroContaStr);
                try {
                    if (rtdb.validarLogin(nome, numeroConta)) {
                        // Se o login for válido, define o login como bem-sucedido
                        loginSuccessful = true;
                        lblStatus.setText("Logado com sucesso!");

                        // Criar e exibir a InterfacePrincipal
                        ContaCorrente conta = new ContaCorrente(numeroConta, 0, true); // Inicialize conforme necessário
                        new InterfacePrincipal(conta, rtdb).setVisible(true); // Passe a instância de RealtimeDatabase aqui
                        dispose(); // Fecha a tela de login
                    } else {
                        lblStatus.setText("Nome ou número da conta inválido.");
                    }
                } catch (InterruptedException ex) {
                    lblStatus.setText("Erro ao conectar ao Firebase: " + ex.getMessage());
                }
            } else {
                lblStatus.setText("Digite um número de conta.");
            }
            notificarListeners();
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public String getNome() {
        return campoNome.getText();
    }

    public int getNumeroConta() {
        String numeroContaStr = campoNumeroConta.getText();
        if (!numeroContaStr.isEmpty()) {
            return Integer.parseInt(numeroContaStr);
        } else {
            return -1; // Retorna -1 se o campo estiver vazio
        }
    }

    public void addLoginListener(ActionListener listener) {
        loginListeners.add(listener);
    }

    private void notificarListeners() {
        for (ActionListener listener : loginListeners) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }
}
