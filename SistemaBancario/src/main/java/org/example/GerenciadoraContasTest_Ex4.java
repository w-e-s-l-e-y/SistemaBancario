package org.example;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe de teste criada para garantir o funcionamento das principais operações
 * sobre contas, realizadas pela classe {@link GerenciadoraContas}.
 */
public class GerenciadoraContasTest_Ex4 {
    private GerenciadoraContas gerContas;
    private Connection connection;

    /**
     * Método para estabelecer a conexão com o banco de dados antes de cada teste.
     */
    public void setUp() {
        // Estabelecer a conexão com o banco de dados SQLite
        String url = "jdbc:sqlite:C:\\Users\\fluib\\Documents\\GitHub\\senac\\SistemaBancario\\SistemaBancario\\src\\main\\java\\org\\example\\wykbank.db";
        try {
            connection = DriverManager.getConnection(url);
            gerContas = new GerenciadoraContas(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Teste básico da transferência de um valor da conta de um cliente para outro,
     * estando ambos os clientes ativos e havendo saldo suficiente para tal transferência ocorrer com sucesso.
     */
    @Test
    public void testTransfereValor() {
        /* Montagem do cenário */
        setUp(); // Configurar a conexão com o banco de dados

        // Criando algumas contas correntes
        ContaCorrente conta01 = new ContaCorrente(1, 200, true);
        ContaCorrente conta02 = new ContaCorrente(2, 0, true);

        try {
            gerContas.adicionarConta(conta01);
            gerContas.adicionarConta(conta02);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* Execução */
        boolean sucesso = false;
        try {
            sucesso = gerContas.transferir(1, 2, 100);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /* Verificações */
        assertTrue(sucesso);
        try {
            assertEquals(100.0, gerContas.verificarSaldo(2), 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
