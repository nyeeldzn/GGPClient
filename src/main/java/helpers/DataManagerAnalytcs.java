package helpers;

import javafx.collections.ObservableList;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class DataManagerAnalytcs {
    private static Connection connection = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    private static ArrayList<Integer> listaTotalData = new ArrayList<>();
    private static ArrayList<Integer> listaTotalHoras = new ArrayList<>();
    private static ArrayList<Integer> listaHorasTriagem = new ArrayList<>();
    public static boolean isFinished = false;
    public static ArrayList<Integer> getListaTotalData() {
        return listaTotalData;
    }
    public static ArrayList<Integer> getListaTotalHorasTriagem() {
        return listaHorasTriagem;
    }
    public boolean isFinished() {
        return isFinished;
    }
    public static PedidoEstatistica createEstatistica(int pedido_id, String hTriagem, String hCheckout, String hFinalizado, String horaPedido) {

        int horario_inicial = toMinutes(hTriagem);

        int horario_checkout = toMinutes(hCheckout);
        int horario_final = toMinutes(hFinalizado);
        double mt = horario_checkout - horario_inicial;
        double me = horario_final - horario_checkout;
        int mp = toHour(hFinalizado);

        PedidoEstatistica estatistica = new PedidoEstatistica(pedido_id, mt, me, mp);
        System.out.println("Tempo de Listagem: " + mt + " Tempo de Entrega: " + me + " Horario Pedido: " + horaPedido);
        return estatistica;
    }
    public static ArrayList<String> getDatasArray(int dias, ArrayList<String> array, String dataInicial) {
        try {
            array = filtrarDadosPorData(dataInicial, dias);
            System.out.println("Datas de busca: " + array);
            boolean next = recuperarQtdPedidos(array, dias);
            if(next == true){
                System.out.println(listaTotalData);
                isFinished = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return array;
    }
    public static ArrayList<String> getDatasArrayMT(int dias, ArrayList<String> array, String dataInicial) {
        try {
            array = filtrarDadosPorData(dataInicial, dias);
            System.out.println("Datas de busca: " + array);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return array;
    }
    public static ArrayList<String> getHorariosArray(int dias, ArrayList<String> array, ObservableList listaPedidoFiltrados, String dataInicial) {

        array = new ArrayList<String>();
        System.out.println("Horas informadas: " + dias);
        for(int i = 0; i < dias; i++){
            array.add(String.valueOf(6+i));
        }
        System.out.println("Horas de busca: " + array);
        boolean next = recuperarQtdPedidosPhora(array, dias);
        if(next == true){
            System.out.println(listaTotalData);
            isFinished = true;
        }

        return array;
    }
    public static ArrayList<String> getMediaTempoTriagem(int dias, ArrayList<String> array, ObservableList listaPedidoFiltrados, String dataInicial) {

        dias = 3;
        array = new ArrayList<String>();
        System.out.println("Horas informadas: " + dias);

        //array.add(String.valueOf());
        array.add("1");
        array.add("2");
        array.add("3");

        System.out.println("Horas de busca: " + array);
        boolean next = recuperarHorarioTriagem(array, dias);
        if(next == true){
            System.out.println(listaHorasTriagem);
            isFinished = true;
        }

        return array;
    }
    public static ArrayList<String> getMTporDia(int dias, ArrayList<String> array) {

        System.out.println("Horas de busca: " + array);
        boolean next = recuperarHorarioTriagem(array, dias);
        if(next == true){
            System.out.println(listaHorasTriagem);
            isFinished = true;
        }

        return array;
    }
    private static boolean recuperarQtdPedidos(ArrayList<String> arrayDatas, int qtdDias) {
        boolean isFinalizado = false;
        for (int i = 0; i < qtdDias; i++) {
                //buscar na data de arrayDatas.get(i)
                //listaTotalData.add(listaTemp.size());
            if (i == qtdDias - 1) {
                isFinalizado = true;
            }
        }
        return isFinalizado;
    }
    private static boolean recuperarQtdPedidosPhora(ArrayList<String> arrayDatas, int qtdHoras) {
        boolean isFinalizado = false;
        for (int i = 0; i < qtdHoras; i++) {
                //busca na data de arraysData.get(i)
                //listaTotalData.add(listaTemp.size());
            if (i == qtdHoras - 1) {
                isFinalizado = true;
            }
        }
        return isFinalizado;
    }
    private static boolean recuperarHorarioTriagem(ArrayList<String> arrayDatas, int qtdHoras) {
        boolean isFinalizado = false;
        for (int i = 0; i < qtdHoras; i++) {
                //buscar no hora de triagem de arraysData.get(i)
                //listaHorasTriagem.add(listaTemp.size());
            if (i == qtdHoras - 1) {
                isFinalizado = true;
            }
        }
        return isFinalizado;
    }
    private static ArrayList<String> filtrarDadosPorData(String data, int qtdDias) throws ParseException {
        System.out.println("Iniciando busca por data");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date data_comparacao = format.parse(data);
        ArrayList<String> listaDatas = new ArrayList<>();
        listaDatas.add(data);
        LocalDateTime localDateTime = data_comparacao.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        for (int i = 0; i < qtdDias; i++) {
            LocalDateTime localDateTime1 = localDateTime.minusDays(1);
            Date date = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());
            SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
            listaDatas.add(dt1.format(date));
            localDateTime = localDateTime1;
        }


        return listaDatas;
    }
    public static int getMTAVGtotal (){
        int value = 0;
        //buscar media de m.t da tabela Pedido_Estatisticas
        return value;
    }
    public static int getMEAVGtotal (){
        int value = 0;
        //buscar media de m.e da tabela Pedido_Estatistica
        return value;
    }
    public static int getPedidostotal (){
        int value = 0;
        //buscar quantidade de pedidos total
        return value;
    }
    public static int getMPC (){
        int value = 0;
        //busca media de pedidos dos por clientes
        return value;
    }

    //Conversores
    public static int toMinutes (String sDur){
        String[] hoursMin = sDur.split(":");

        int iHour = Integer.parseInt(hoursMin[0]);
        int iMin = Integer.parseInt(hoursMin[1]);

        int hoursInMinutes = iHour * 60;
        int horarioFinal = hoursInMinutes + iMin;

        System.out.println("HORARIO: " + sDur + "CONVERTIDO PARA MINUTOS: " + horarioFinal);
        return horarioFinal;
    }
    public static int toHour (String sDur){
        String[] hoursMin = sDur.split(":");

        int iHour = Integer.parseInt(hoursMin[0]);
        //int iMin = Integer.parseInt(hoursMin[1]);



        System.out.println("HORA RECEBIDO: " + sDur + "CONVERTIDO PARA HORAS: " + iHour);
        return iHour;
    }
    //


}
