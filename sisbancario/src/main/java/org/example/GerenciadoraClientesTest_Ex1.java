package org.example;

import static org.junit.Assert.assertThat;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;

public class GerenciadoraClientesTest_Ex1 {

    @Test
    public void testPesquisaCliente() {
        // Criando alguns clientes
        Cliente cliente01 = new Cliente(1, "Maruzam Bueno", 31, "maruzambueno@gmail.com", 1, true);
        Cliente cliente02 = new Cliente(2, "Darth Vader", 34, "dartvader@gmail.com", 2, true);

        // Inserindo os clientes criados na lista de clientes do banco
        List<Cliente> clientesDoBanco = new ArrayList<>();
        clientesDoBanco.add(cliente01);
        clientesDoBanco.add(cliente02);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientesDoBanco);

        Cliente cliente = gerClientes.pesquisaCliente(1);

        assertThat(cliente.getId(), is(1));
        assertThat(cliente.getEmail(), is("maruzambueno@gmail.com"));
    }

    @Test
    public void testRemoveCliente() {
        // Criando alguns clientes
        Cliente cliente01 = new Cliente(1, "Maruzam Bueno", 31, "maruzambueno@gmail.com", 1, true);
        Cliente cliente02 = new Cliente(2, "Darth Vader", 34, "dartvader@gmail.com", 2, true);

        // Inserindo os clientes criados na lista de clientes do banco
        List<Cliente> clientesDoBanco = new ArrayList<>();
        clientesDoBanco.add(cliente01);
        clientesDoBanco.add(cliente02);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientesDoBanco);

        boolean clienteRemovido = gerClientes.removeCliente(2);

        assertThat(clienteRemovido, is(true));
        assertThat(gerClientes.getClientesDoBanco().size(), is(1));
        assertNull(gerClientes.pesquisaCliente(2));
    }

    @Test
    public void testPesquisaCliente_Ex3() {
        // Criando alguns clientes
        Cliente cliente01 = new Cliente(1, "Maruzam Bueno", 31, "maruzambueno@gmail.com", 1, true);
        Cliente cliente02 = new Cliente(2, "Darth Vader", 34, "dartvader@gmail.com", 2, true);

        // Inserindo os clientes criados na lista de clientes do banco
        List<Cliente> clientesDoBanco = new ArrayList<>();
        clientesDoBanco.add(cliente01);
        clientesDoBanco.add(cliente02);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientesDoBanco);

        Cliente cliente = gerClientes.pesquisaCliente(1);

        assertThat(cliente.getId(), is(1));
    }

    @Test
    public void testRemoveCliente_Ex3() {
        // Criando alguns clientes
        Cliente cliente01 = new Cliente(1, "Maruzam Bueno", 31, "maruzambueno@gmail.com", 1, true);
        Cliente cliente02 = new Cliente(2, "Darth Vader", 34, "dartvader@gmail.com", 2, true);

        // Inserindo os clientes criados na lista de clientes do banco
        List<Cliente> clientesDoBanco = new ArrayList<>();
        clientesDoBanco.add(cliente01);
        clientesDoBanco.add(cliente02);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientesDoBanco);

        boolean clienteRemovido = gerClientes.removeCliente(2);

        assertThat(clienteRemovido, is(true));
        assertThat(gerClientes.getClientesDoBanco().size(), is(1));
        assertNull(gerClientes.pesquisaCliente(2));
    }

    @Test
    public void testPesquisaCliente_Ex4() {
        // Criando alguns clientes
        Cliente cliente01 = new Cliente(1, "Maruzam Bueno", 31, "maruzambueno@gmail.com", 1, true);
        Cliente cliente02 = new Cliente(2, "Darth Vader", 34, "dartvader@gmail.com", 2, true);

        // Inserindo os clientes criados na lista de clientes do banco
        List<Cliente> clientesDoBanco = new ArrayList<>();
        clientesDoBanco.add(cliente01);
        clientesDoBanco.add(cliente02);

        GerenciadoraClientes gerClientes = new GerenciadoraClientes(clientesDoBanco);

        Cliente cliente = gerClientes.pesquisaCliente(1);

        assertThat(cliente.getId(), is(1));
    }
}
