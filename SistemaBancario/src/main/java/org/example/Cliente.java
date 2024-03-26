package org.example;

public class Cliente {
    private int id;
    private String nome;
    private int idade;
    private String email;
    private int tipo; // Suponho que o tipo refira-se a algum tipo específico de cliente (ex: pessoa física, pessoa jurídica, etc.)
    private boolean ativo;

    // Construtor
    public Cliente(int id, String nome, int idade, String email, int tipo, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.email = email;
        this.tipo = tipo;
        this.ativo = ativo;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        if (idade >= 0) { // Verificação básica de idade não negativa
            this.idade = idade;
        } else {
            throw new IllegalArgumentException("A idade deve ser um valor não negativo.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        // Verificação básica de formato de e-mail
        if (email.contains("@") && email.contains(".")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("O e-mail fornecido não é válido.");
        }
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        // Adicionar validação de tipo, se necessário
        this.tipo = tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
