package models;

import java.util.ArrayList;
import java.util.List;

public class ListaRuptura {

    Long id;
    String desc;
    String date;

    private List<RupturaProduto> produtoList;

    public ListaRuptura () {}

    public ListaRuptura(Long id, String desc, String date) {
        this.id = id;
        this.desc = desc;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<RupturaProduto> getProdutoList() {
        return produtoList;
    }

    public void setProdutoList(List<RupturaProduto> produtoList) {
        this.produtoList = produtoList;
    }

    public static List<RupturaProduto> produtoListToRupturaProduto (ListaRuptura listaRuptura, List<Produto> produtos) {
        List<RupturaProduto> list = new ArrayList<>();
        for(int i = 0; i < produtos.size(); i++){
            RupturaProduto newObj = new RupturaProduto(null, produtos.get(i),listaRuptura);
            list.add(newObj);
        }
        return list;
    }
}
