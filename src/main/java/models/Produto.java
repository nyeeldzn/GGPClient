package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;


    private String nome;


    private List<OrdemPedido> pedidos = new ArrayList<>();

    public Produto() {
    }

    public Produto(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

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

}
