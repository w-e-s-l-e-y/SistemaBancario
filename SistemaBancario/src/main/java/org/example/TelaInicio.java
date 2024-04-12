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

public class TelaInicio extends JFrame implements ActionListener {
    private JButton btnLogin;
    private JButton btnCadastro;

    public TelaInicio() {
        setTitle("Banco WYK");
        setSize(400, 300); // Ajusta o tamanho da janela
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adiciona um JLabel com o texto "Bem vindo ao Banco WYK" em caixa alta
        JLabel lblBemVindo = new JLabel("BEM VINDO AO BANCO WYK");
        lblBemVindo.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza o texto
        lblBemVindo.setFont(lblBemVindo.getFont().deriveFont(Font.BOLD, 18f)); // Define a fonte em negrito e tamanho 18

        btnLogin = new JButton("Login");
        btnCadastro = new JButton("Cadastro");

        // Cria um JPanel para o título e os botões
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new GridBagLayout()); // Usando GridBagLayout para melhor controle de posicionamento

        // Adiciona o JLabel ao JPanel
        GridBagConstraints gbcLabel = new GridBagConstraints();
        gbcLabel.gridx = 0;
        gbcLabel.gridy = 0;
        gbcLabel.gridwidth = 1;
        gbcLabel.gridheight = 1;
        gbcLabel.weightx = 1.0;
        gbcLabel.weighty = 0.5;
        gbcLabel.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(lblBemVindo, gbcLabel);

        // Adiciona o JPanel com os botões ao JPanel principal
        GridBagConstraints gbcBotoes = new GridBagConstraints();
        gbcBotoes.gridx = 0;
        gbcBotoes.gridy = 1;
        gbcBotoes.gridwidth = 1;
        gbcBotoes.gridheight = 1;
        gbcBotoes.weightx = 1.0;
        gbcBotoes.weighty = 0.5;
        gbcBotoes.fill = GridBagConstraints.HORIZONTAL;
        panelPrincipal.add(criarPainelBotoes(), gbcBotoes);

        // Adiciona o panelPrincipal à janela
        add(panelPrincipal);

        // Adiciona ActionListener aos botões
        btnLogin.addActionListener(this);
        btnCadastro.addActionListener(this);
    }

    // Método para criar o JPanel com os botões
    private JPanel criarPainelBotoes() {
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotoes.add(btnLogin);
        panelBotoes.add(btnCadastro);
        return panelBotoes;
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
                    // Recupera os dados inseridos pelo usuário na tela de login
                    String nome = telaLogin.getNome();
                    int numeroConta = telaLogin.getNumeroConta();

                    // Verifica se os dados correspondem a um registro no banco de dados
                    ContaCorrente conta = obterContaDoBancoDeDados(nome, numeroConta);

                    if (conta != null) {
                        fecharTelaInicio(); // Fechar a tela de início
                        abrirInterfacePrincipal(conta); // Passa a instância válida de ContaCorrente
                        telaLogin.dispose(); // Fecha a tela de login após o login bem-sucedido
                        JOptionPane.showMessageDialog(null, "Logado com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Nome ou número da conta inválidos.");
                    }
                }
            }
        });
        telaLogin.setVisible(true);
    }

    private ContaCorrente obterContaDoBancoDeDados(String nome, int numeroConta) {
        String url = "jdbc:sqlite:C:\\Users\\964610\\Documents\\GitHub\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db";
        try (Connection connection = DriverManager.getConnection(url)) {
            String sql = "SELECT id, saldo, ativa " +
                    "FROM ContaCorrente " +
                    "WHERE cliente_id = (SELECT id FROM Cliente WHERE nome = ?) " +
                    "AND id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, nome);
                statement.setInt(2, numeroConta);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        double saldo = resultSet.getDouble("saldo");
                        boolean ativa = resultSet.getBoolean("ativa");
                        return new ContaCorrente(id, saldo, ativa);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Adiciona essa linha para imprimir o stack trace da exceção
            JOptionPane.showMessageDialog(null, "Erro ao consultar o banco de dados: " + ex.getMessage());
        }
        return null;
    }

    private void fecharTelaInicio() {
        dispose(); // Fecha a tela de início
    }

    private void abrirInterfacePrincipal(ContaCorrente conta) {
        InterfacePrincipal interfacePrincipal = new InterfacePrincipal(conta);
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
