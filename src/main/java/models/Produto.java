package models;

import javafx.collections.ObservableList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;


    private Long id;


    private String nome;


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

    public OrderProduct toOrderProduct (OrdemPedido pedido, int quantity){
        OrderProduct order = new OrderProduct(pedido,this,quantity);
        return order;
    }


    public List<OrderProduct> toList (ObservableList<OrderProduct> prods){
        List<OrderProduct> list = new ArrayList<>();
        for(int i = 0; i<prods.size(); i++){
            list.add(prods.get(i));
        }
        return list;
    }

}
