package org.example;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ContaTest {

    @Test
    void testCreditar() {
        // Testa a funcionalidade de creditar dinheiro na conta.
        Conta conta = new Conta(12345, 1000.00);
        conta.creditar(200.00);
        // Verifica se o saldo é atualizado corretamente após o crédito.
        assertEquals(1200.00, conta.getSaldo());
    }

    @Test
    void testDebitar() {
        // Testa a funcionalidade de debitar dinheiro da conta.
        Conta conta = new Conta(12345, 1000.00);
        conta.debitar(500.00);
        // Verifica se o saldo é atualizado corretamente após o débito.
        assertEquals(500.00, conta.getSaldo());
    }

    @Test
    void testDebitarComSaldoInsuficiente() {
        // Testa se ocorre uma exceção ao tentar debitar um valor maior do que o saldo disponível.
        Conta conta = new Conta(12345, 100.00);
        // Verifica se uma exceção é lançada ao tentar debitar mais do que o saldo disponível.
        assertThrows(IllegalArgumentException.class, () -> conta.debitar(200.00));
    }

    @Test
    void testRetirarChequeEspecialDentroDoLimite() {
        // Testa a funcionalidade de retirar dinheiro usando o cheque especial dentro do limite disponível.
        Conta conta = new Conta(12345, 50.00);
        conta.retirarChequeEspecial(100.00); // Usando o cheque especial.
        // Verifica se o saldo é atualizado corretamente após a retirada do cheque especial.
        assertEquals(-50.00, conta.getSaldo());
    }

    @Test
    void testRetirarChequeEspecialExcedendoLimite() {
        // Testa se ocorre uma exceção ao tentar retirar um valor que excede o limite do cheque especial.
        Conta conta = new Conta(12345, 50.00);
        // Verifica se uma exceção é lançada ao tentar retirar mais do que o limite do cheque especial.
        assertThrows(IllegalArgumentException.class, () -> conta.retirarChequeEspecial(160.00));
    }
}
