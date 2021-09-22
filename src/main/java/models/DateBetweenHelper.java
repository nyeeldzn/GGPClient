package models;

public class DateBetweenHelper {

    private String dataInicial;
    private String dataFinal;


    public DateBetweenHelper(String dataInicial, String dataFinal) {
        this.dataInicial = dataInicial;
        this.dataFinal = dataFinal;
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

    @Override
    public String toString() {
        return "DateBetweenHelper{" +
                "dataInicial= " + dataInicial +
                ", dataFinal= " + dataFinal +
                '}';
    }
}

