package models;


public class OrderProduct {

    private Long id;

    private Produto produto;

    private Integer quantity;

    public OrderProduct(){}

    public OrderProduct(OrdemPedido pedido, Produto produto, Integer quantity) {
        this.produto = produto;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


}
