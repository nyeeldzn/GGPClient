package models;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private Long id;
    private String nome;
    private String endereco;
    private Bairro bairro;
    private String telefone;
    String data_cadastro;

    private List<OrdemPedido> pedidos = new ArrayList<>();

    public Cliente(Long id, String nome, String endereco, Bairro bairro, String telefone, String data_cadastro) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.bairro = bairro;
        this.telefone = telefone;
        this.data_cadastro = data_cadastro;
    }

    public Cliente(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getData_cadastro() {
        return data_cadastro;
    }

    public void setData_cadastro(String data_cadastro) {
        this.data_cadastro = data_cadastro;
    }

    public List<OrdemPedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<OrdemPedido> pedidos) {
        this.pedidos = pedidos;
    }
}
