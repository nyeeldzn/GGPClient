package models;

import java.util.List;


public class Bairro {

    private Long id;


    private String nome;


    private List<Cliente> clientes;

    public Bairro () {}

    public Bairro (Long id, String nome){
        this.id = id;
        this.nome = nome;
    }

}
