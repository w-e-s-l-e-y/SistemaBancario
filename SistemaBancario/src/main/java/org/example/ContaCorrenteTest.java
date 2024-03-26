
package org.example;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContaCorrenteTest {

    @Test
    public void testAbrirContaComDeposito() {
        // Defina os parâmetros para a abertura da conta
        int id = 1;
        double saldoInicial = 100.0; // Saldo mínimo inicial
        boolean ativa = true;

        // Tente abrir uma conta com depósito inicial
        ContaCorrente conta = ContaCorrente.abrirContaComDeposito(id, saldoInicial);

        // Verifique se a conta foi aberta corretamente
        assertNotNull(conta);
        assertEquals(id, conta.getId());
        assertEquals(saldoInicial, conta.getSaldo(), 0.01); // Use um delta pequeno para lidar com possíveis erros de arredondamento
        assertEquals(ativa, conta.isAtiva());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirContaComSaldoInicialNegativo() {
        // Defina um saldo inicial negativo
        int id = 2;
        double saldoInicialNegativo = -100.0;

        // Deve lançar uma exceção ao tentar abrir uma conta com saldo inicial negativo
        ContaCorrente.abrirContaComDeposito(id, saldoInicialNegativo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAbrirContaComSaldoInicialZero() {
        // Defina um saldo inicial zero
        int id = 3;
        double saldoInicialZero = 0.0;

        // Deve lançar uma exceção ao tentar abrir uma conta com saldo inicial zero
        ContaCorrente.abrirContaComDeposito(id, saldoInicialZero);
    }
}
