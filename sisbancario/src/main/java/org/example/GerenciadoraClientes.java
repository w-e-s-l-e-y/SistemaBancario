package org.example;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe de negócio para realizar operações sobre os clientes do banco.
 */
public class GerenciadoraClientes {

    private Map<Integer, Cliente> clientesMap; // Usando um mapa para armazenar os clientes por ID

    /**
     * Construtor da classe GerenciadoraClientes.
     * @param clientes Lista de clientes do banco
     */
    public GerenciadoraClientes(List<Cliente> clientes) {
        this.clientesMap = new HashMap<>();
        for (Cliente cliente : clientes) {
            clientesMap.put(cliente.getId(), cliente);
        }
    }

    /**
     * Retorna uma lista com todos os clientes do banco.
     * @return Lista com todos os clientes do banco
     */
    public List<Cliente> getClientesDoBanco() {
        return List.copyOf(clientesMap.values());
    }

    /**
     * Pesquisa por um cliente a partir do seu ID.
     * @param idCliente ID do cliente a ser pesquisado
     * @return O cliente pesquisado ou null, caso não seja encontrado
     */
    public Cliente pesquisaCliente(int idCliente) {
        return clientesMap.getOrDefault(idCliente, null);
    }

    /**
     * Remove um cliente do banco.
     * @param idCliente ID do cliente a ser removido
     * @return true se o cliente foi removido com sucesso, false caso contrário
     */
    public boolean removeCliente(int idCliente) {
        return clientesMap.remove(idCliente) != null;
    }
}
