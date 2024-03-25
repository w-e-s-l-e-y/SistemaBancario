package org.example;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class GerenciadoraContasTest_Ex3 {

    private GerenciadoraContas gerContas;

    @Test
    public void testTransfereValor() {
        /*
        Montagem do cenário
        // Criando algumas contas
        */
        ContaCorrente conta01 = new ContaCorrente(1, 200, true);
        ContaCorrente conta02 = new ContaCorrente(2, 0, true);

        /*
        // Inserindo as contas criadas na lista de contas do banco
        */
        List<ContaCorrente> contasDoBanco = new ArrayList<>();
        contasDoBanco.add(conta01);
        contasDoBanco.add(conta02);

        gerContas = new GerenciadoraContas(contasDoBanco);

        /*
        Execução
        */
        boolean sucesso = gerContas.transferir(1, 2, 100); // Corrigido para chamar o método transferir

        /*
        Verificações
        */
        assertTrue(sucesso);
        assertThat(conta02.getSaldo(), is(100.0));
        assertThat(conta01.getSaldo(), is(100.0));
    }
}
