package models;

public class RupturaProduto {


    private Long id;

    private Produto produto;

    public RupturaProduto(){}

    public RupturaProduto(Long id, Produto produto, ListaRuptura listaRuptura) {
        this.id = id;
        this.produto = produto;
        //this.listaRuptura = listaRuptura;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //public ListaRuptura getListaRuptura() {
   //     return listaRuptura;
   // }

   // public void setListaRuptura(ListaRuptura listaRuptura) {
   //     this.listaRuptura = listaRuptura;
  //  }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }


}
