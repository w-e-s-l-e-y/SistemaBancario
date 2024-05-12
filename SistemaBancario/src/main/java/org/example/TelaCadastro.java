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

public class TelaCadastro extends JFrame implements ActionListener {
    private JTextField campoNome;
    private JTextField campoIdade;
    private JTextField campoEmail;
    private JTextField campoTipo;
    private JTextField campoSaldoInicial;
    private JButton btnCadastrar;

    public TelaCadastro() {
        setTitle("Cadastro de Cliente");
        setSize(400, 350); // Aumenta o tamanho da janela
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // Adiciona uma margem ao redor do painel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Adiciona margens de 20px em todos os lados

        // Define um painel para os campos de entrada com GridBagLayout
        JPanel panelCampos = new JPanel(new GridBagLayout());

        // Adiciona rótulos e campos de entrada ao painel de campos usando GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // Adiciona um espaço de 5px entre os componentes
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridy++;
        panelCampos.add(new JLabel("Idade:"), gbc);
        gbc.gridy++;
        panelCampos.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        panelCampos.add(new JLabel("Tipo:"), gbc);
        gbc.gridy++;
        panelCampos.add(new JLabel("Saldo Inicial:"), gbc);






        gbc.gridx = 1;
        gbc.gridy = 0;
        campoNome = new JTextField(20);
        panelCampos.add(campoNome, gbc);
        gbc.gridy++;
        campoIdade = new JTextField(20);
        panelCampos.add(campoIdade, gbc);
        gbc.gridy++;
        campoEmail = new JTextField(20);
        panelCampos.add(campoEmail, gbc);
        gbc.gridy++;
        campoTipo = new JTextField(20);
        panelCampos.add(campoTipo, gbc);
        gbc.gridy++;
        campoSaldoInicial = new JTextField(20);
        panelCampos.add(campoSaldoInicial, gbc);

        // Adiciona o painel de campos ao painel principal
        panelPrincipal.add(panelCampos, BorderLayout.CENTER);

        // Cria um painel para o botão de cadastro e adiciona ao painel principal
        JPanel panelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color (135, 206, 250));
        btnCadastrar.addActionListener(this);
        panelBotao.add(btnCadastrar);
        panelPrincipal.add(panelBotao, BorderLayout.SOUTH);

        // Define a cor de fundo do painel principal
        panelPrincipal.setBackground(Color.lightGray);

        // Adiciona o painel principal à janela
        add(panelPrincipal);
    }

    // Cria um painel para o botão de cadastro e adiciona ao painel principal
    JPanel panelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));




    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCadastrar) {
            // Obter os dados dos campos de texto
            String nome = campoNome.getText();
            int idade = Integer.parseInt(campoIdade.getText());
            String email = campoEmail.getText();
            int tipo = Integer.parseInt(campoTipo.getText());

            // Estabelecer a conexão com o banco de dados SQLite C:\Users\fluib\Documents\GitHub\senac\SistemaBancario\SistemaBancario\src\main\java\org\example
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:E:\\SistemaBancario\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db")) {
                // Inserir o cliente na tabela Cliente
                String sqlCliente = "INSERT INTO Cliente (nome, idade, email, tipo, ativo) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sqlCliente, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, nome);
                    statement.setInt(2, idade);
                    statement.setString(3, email);
                    statement.setInt(4, tipo);
                    statement.setBoolean(5, true); // Supondo que o cliente é sempre cadastrado como ativo

                    // Executar a instrução SQL para inserir o cliente
                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        // Recuperar o id do cliente recém-inserido
                        ResultSet generatedKeys = statement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int clienteId = generatedKeys.getInt(1);
                            // Inserir uma nova conta associada ao cliente na tabela Conta
                            String sqlConta = "INSERT INTO ContaCorrente (saldo, ativa, cliente_id, cheque_especial) VALUES (?, ?, ?, ?)";
                            try (PreparedStatement contaStatement = connection.prepareStatement(sqlConta, PreparedStatement.RETURN_GENERATED_KEYS)) {
                                contaStatement.setDouble(1, Double.parseDouble(campoSaldoInicial.getText())); // Saldo inicial 0
                                contaStatement.setBoolean(2, true); // Conta ativa
                                contaStatement.setInt(3, clienteId); // Id do cliente
                                contaStatement.setInt(4, (int) (Double.parseDouble(campoSaldoInicial.getText()) / 3)); // cheque especial do cliente
                                int contaInserted = contaStatement.executeUpdate();
                                if (contaInserted > 0) {
                                    ResultSet contaGeneratedKeys = contaStatement.getGeneratedKeys();
                                    if (contaGeneratedKeys.next()) {
                                        int contaId = contaGeneratedKeys.getInt(1);
                                        JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso! O número da conta é: " + contaId);
                                        this.dispose(); // Fechar a tela de cadastro
                                    }
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente.");
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TelaCadastro().setVisible(true);
            }
        });
    }
}
