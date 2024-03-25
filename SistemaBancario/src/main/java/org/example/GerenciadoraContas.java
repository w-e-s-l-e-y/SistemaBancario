package org.example;

import java.util.ArrayList;
import java.util.List;

public class GerenciadoraContas {
    private List<ContaCorrente> contas; // Alterado para List<ContaCorrente>

    public GerenciadoraContas(List<ContaCorrente> contas) {
        this.contas = contas;
    }

    public GerenciadoraContas() {
        this.contas = new ArrayList<>();
    }

    // Método para adicionar uma nova conta corrente
    public void adicionarConta(ContaCorrente conta) { // Alterado o parâmetro para ContaCorrente
        contas.add(conta);
    }

    // Método para remover uma conta corrente
    public boolean removerConta(int numeroConta) {
        ContaCorrente contaRemover = buscarContaPorNumero(numeroConta); // Alterado para ContaCorrente
        return contas.remove(contaRemover);
    }

    // Método para verificar o saldo de uma conta corrente
    public double verificarSaldo(int numeroConta) {
        ContaCorrente conta = buscarContaPorNumero(numeroConta); // Alterado para ContaCorrente
        return (conta != null) ? conta.getSaldo() : -1;
    }

    // Método para transferir dinheiro entre contas correntes
    public boolean transferir(int contaOrigem, int contaDestino, double valor) {
        ContaCorrente origem = buscarContaPorNumero(contaOrigem); // Alterado para ContaCorrente
        ContaCorrente destino = buscarContaPorNumero(contaDestino); // Alterado para ContaCorrente

        if (origem != null && destino != null && origem.getSaldo() >= valor) {
            origem.debitar(valor);
            destino.creditar(valor);
            return true; // Transferência realizada com sucesso
        } else {
            return false; // Saldo insuficiente na conta de origem ou contas não encontradas
        }
    }

    // Método para obter a lista de contas correntes
    public List<ContaCorrente> getContas() { // Alterado para List<ContaCorrente>
        return contas;
    }

    // Método para buscar uma conta corrente pelo número
    private ContaCorrente buscarContaPorNumero(int numeroConta) { // Alterado para ContaCorrente
        for (ContaCorrente conta : contas) { // Alterado para ContaCorrente
            if (conta.getNumero() == numeroConta) {
                return conta;
            }
        }
        return null; // Conta não encontrada
    }
}
