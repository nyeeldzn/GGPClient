package models;

public class PedidoFindJsonHelper {

    private String dataInicial;
    private String dataFinal;
    private Cliente cliente;


    public PedidoFindJsonHelper(String dataInicial, String dataFinal) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.cliente = null;
    }

    public PedidoFindJsonHelper(String dataInicial, String dataFinal, Cliente cliente) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
        this.cliente = cliente;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "PedidoFindJsonHelper{" +
                "dataInicial='" + dataInicial + '\'' +
                ", dataFinal='" + dataFinal + '\'' +
                ", cliente=" + cliente +
                '}';
    }
}

