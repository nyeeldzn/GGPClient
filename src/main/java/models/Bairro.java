package models;

import java.util.Collection;
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

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    @Override
    public String toString() {
        return nome;
    }

    public static Bairro findByNome(Collection<Bairro> listBairro, String nome) {
        return listBairro.stream().filter(carnet -> nome.equals(carnet.getNome())).findFirst().orElse(null);
    }
}
