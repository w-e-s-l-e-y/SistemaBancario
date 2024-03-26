package org.example;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ContaCorrenteTest {

    // Método auxiliar para obter uma conexão com o banco de dados SQLite
    private Connection getConnection() throws SQLException {
        // Use um caminho relativo para o banco de dados
        String url = "jdbc:sqlite:src/main/java/org/example/wykbank.db";
        return DriverManager.getConnection(url);
    }

    @Test
    public void testAbrirContaComDeposito() {
        // Defina os parâmetros para a abertura da conta
        int id = 1;
        double saldoInicial = 100.0; // Saldo mínimo inicial
        boolean ativa = true;

        try (Connection connection = getConnection()) {
            // Tente abrir uma conta com depósito inicial
            ContaCorrente conta = ContaCorrente.abrirContaComDeposito(id, saldoInicial, connection);

            // Verifique se a conta foi aberta corretamente
            assertNotNull(conta);
            assertEquals(id, conta.getId());
            assertEquals(saldoInicial, conta.getSaldo(), 0.01); // Use um delta pequeno para lidar com possíveis erros de arredondamento
            assertEquals(ativa, conta.isAtiva());
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Erro ao obter conexão com o banco de dados.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirContaComSaldoInicialNegativo() {
        // Defina um saldo inicial negativo
        int id = 2;
        double saldoInicialNegativo = -100.0;

        try (Connection connection = getConnection()) {
            // Deve lançar uma exceção ao tentar abrir uma conta com saldo inicial negativo
            ContaCorrente.abrirContaComDeposito(id, saldoInicialNegativo, connection);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Erro ao obter conexão com o banco de dados.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirContaComSaldoInicialZero() {
        // Defina um saldo inicial zero
        int id = 3;
        double saldoInicialZero = 0.0;

        try (Connection connection = getConnection()) {
            // Deve lançar uma exceção ao tentar abrir uma conta com saldo inicial zero
            ContaCorrente.abrirContaComDeposito(id, saldoInicialZero, connection);
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Erro ao obter conexão com o banco de dados.");
        }
    }
}
