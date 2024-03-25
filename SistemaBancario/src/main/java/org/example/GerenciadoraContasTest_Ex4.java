package org.example;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de teste criada para garantir o funcionamento das principais operações
 * sobre contas, realizadas pela classe {@link GerenciadoraContas}.
 */
public class GerenciadoraContasTest_Ex4 {
    private GerenciadoraContas gerContas;

    /**
     * Teste básico da transferência de um valor da conta de um cliente para outro,
     * estando ambos os clientes ativos e havendo saldo suficiente para tal transferência ocorrer com sucesso.
     */
    @Test
    public void testTransfereValor() {
        /* Montagem do cenário */
        // Criando algumas contas correntes
        ContaCorrente conta01 = new ContaCorrente(1, 200, true);
        ContaCorrente conta02 = new ContaCorrente(2, 0, true);

        // Inserindo as contas criadas na lista de contas do banco
        List<ContaCorrente> contasDoBanco = new ArrayList<>();
        contasDoBanco.add(conta01);
        contasDoBanco.add(conta02);

        gerContas = new GerenciadoraContas(contasDoBanco);

        /* Execução */
        boolean sucesso = gerContas.transferir(1, 2, 100); // Aqui a chamada correta do método é transferir, não transfereValor

        /* Verificações */
        assertTrue(sucesso);
        assertThat(conta02.getSaldo(), is(100.0));
    }
}
