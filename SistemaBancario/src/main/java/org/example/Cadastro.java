package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cadastro extends JFrame implements ActionListener {
    // Componentes da tela
    private JTextField campoNome, campoIdade, campoEmail;
    private JComboBox<String> comboBoxTipo;
    private JCheckBox checkBoxAtivo;
    private JButton botaoCadastrar;

    public Cadastro() {
        // Configurações da janela
        setTitle("Cadastro de Cliente");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criação dos componentes
        campoNome = new JTextField(20);
        campoIdade = new JTextField(20);
        campoEmail = new JTextField(20);
        comboBoxTipo = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});
        checkBoxAtivo = new JCheckBox("Ativo");
        botaoCadastrar = new JButton("Cadastrar");

        // Configuração do layout
        setLayout(new GridLayout(6, 1));

        // Adiciona os componentes à tela
        add(new JLabel("Nome:"));
        add(campoNome);
        add(new JLabel("Idade:"));
        add(campoIdade);
        add(new JLabel("Email:"));
        add(campoEmail);
        add(new JLabel("Tipo:"));
        add(comboBoxTipo);
        add(checkBoxAtivo);
        add(botaoCadastrar);

        // Associa o botão ao evento de clique
        botaoCadastrar.addActionListener(this);
    }

    // Método executado quando o botão é clicado
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == botaoCadastrar) {
            // Obtém os valores dos campos
            String nome = campoNome.getText();
            int idade = Integer.parseInt(campoIdade.getText());
            String email = campoEmail.getText();
            String tipo = (String) comboBoxTipo.getSelectedItem();
            boolean ativo = checkBoxAtivo.isSelected();

            // Simplesmente exibe os valores, mas aqui você poderia realizar outras ações, como salvar em um banco de dados
            JOptionPane.showMessageDialog(this, "Cliente cadastrado:\nNome: " + nome + "\nIdade: " + idade + "\nEmail: " + email + "\nTipo: " + tipo + "\nAtivo: " + ativo);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Cadastro().setVisible(true);
            }
        });
    }
}
