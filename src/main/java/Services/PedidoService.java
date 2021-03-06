package Services;

import DTO.OrdemPedidoDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import helpers.HTTPRequest.DefaultRequests;
import models.OrdemPedido;
import models.PedidoFindJsonHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {

        public static OrdemPedido getById(Long id){
            OrdemPedido ped = new OrdemPedido();
            try {
                String json = DefaultRequests.getObject(id.toString(), "/pedidos");

                Gson gson = new Gson();
                ped = gson.fromJson(new String(json.getBytes()), OrdemPedido.class);
            }catch (IOException e){
                e.printStackTrace();
            }
            return ped;
        }


        public static ArrayList<OrdemPedido> findAll(){
            ArrayList<OrdemPedidoDTO> pedidosDTO = findAllDTO();
            ArrayList<OrdemPedido> pedidos = new ArrayList<>();
            for(int i = 0; i<pedidosDTO.size(); i++){
                pedidos.add(OrdemPedidoDTO.toOrdemPedido(pedidosDTO.get(i)));
            }

            return pedidos;
        }

    public static ArrayList<OrdemPedido> findAllByStatus(Integer status){
        ArrayList<OrdemPedidoDTO> pedidosDTO = findAllDTOByStatus(status);
        ArrayList<OrdemPedido> pedidos = new ArrayList<>();
        for(int i = 0; i<pedidosDTO.size(); i++){
            pedidos.add(OrdemPedidoDTO.toOrdemPedido(pedidosDTO.get(i)));
        }

        return pedidos;
    }

    public static ArrayList<OrdemPedido> findAllByDate(PedidoFindJsonHelper dBH){
        ArrayList<OrdemPedidoDTO> pedidosDTO = findAllDTOByDate(dBH);
        ArrayList<OrdemPedido> pedidos = new ArrayList<>();
        for(int i = 0; i<pedidosDTO.size(); i++){
            pedidos.add(OrdemPedidoDTO.toOrdemPedido(pedidosDTO.get(i)));
        }

        return pedidos;
    }

    public static ArrayList<OrdemPedido> findAllByDateWithStatus(PedidoFindJsonHelper dBH, Integer status){
        ArrayList<OrdemPedidoDTO> pedidosDTO = findAllDTOByDateWithStatus(dBH, status);
        ArrayList<OrdemPedido> pedidos = new ArrayList<>();
        for(int i = 0; i<pedidosDTO.size(); i++){
            pedidos.add(OrdemPedidoDTO.toOrdemPedido(pedidosDTO.get(i)));
        }

        return pedidos;
    }



        public static ArrayList<OrdemPedidoDTO> findAllDTO(){
            ArrayList<OrdemPedidoDTO> pedidosDTO = new ArrayList<>();
            String json = DefaultRequests.getAll("/pedidos");

            Gson gson = new Gson();

            Type userListType = new TypeToken<ArrayList<OrdemPedidoDTO>>(){}.getType();

            pedidosDTO = gson.fromJson(json, userListType);


            return pedidosDTO;
        }

    public static ArrayList<OrdemPedidoDTO> findAllDTOByStatus( Integer status ){
        ArrayList<OrdemPedidoDTO> pedidosDTO = new ArrayList<>();

        try {
            String json = DefaultRequests.getObject(status.toString(), "/pedidos/buscaPorStatus");

            Gson gson = new Gson();

            Type userListType = new TypeToken<ArrayList<OrdemPedidoDTO>>() {
            }.getType();

            pedidosDTO = gson.fromJson(json, userListType);
        }catch (IOException ioe){
           ioe.printStackTrace();
        }


        return pedidosDTO;
    }

    public static ArrayList<OrdemPedidoDTO> findAllDTOByDate(PedidoFindJsonHelper dBH){
        ArrayList<OrdemPedidoDTO> pedidosDTO = new ArrayList<>();
        Gson gson = new Gson();
        String dBHJson = gson.toJson(dBH);
        String output = DefaultRequests.postObject("/pedidos/buscaPorData", dBHJson);

        Gson gson2 = new Gson();

        Type userListType = new TypeToken<ArrayList<OrdemPedidoDTO>>() {
        }.getType();

        pedidosDTO = gson2.fromJson(output, userListType);



        return pedidosDTO;
    }

    public static ArrayList<OrdemPedidoDTO> findAllDTOByDateWithStatus(PedidoFindJsonHelper dBH, Integer status){
        ArrayList<OrdemPedidoDTO> pedidosDTO = new ArrayList<>();
        Gson gson = new Gson();
        String dBHJson = gson.toJson(dBH);
        String output = DefaultRequests.postObject("/pedidos/buscaPorDataComStatus/" + status, dBHJson);

        Gson gson2 = new Gson();

        Type userListType = new TypeToken<ArrayList<OrdemPedidoDTO>>() {
        }.getType();

        pedidosDTO = gson2.fromJson(output, userListType);


        return pedidosDTO;
    }


    //Filtro por Cliente
    public static ArrayList<OrdemPedido> findAllByDateFromClient(PedidoFindJsonHelper dBH){
        ArrayList<OrdemPedidoDTO> pedidosDTO = findAllDTOByDateFromClient(dBH);
        ArrayList<OrdemPedido> pedidos = new ArrayList<>();
        for(int i = 0; i<pedidosDTO.size(); i++){
            pedidos.add(OrdemPedidoDTO.toOrdemPedido(pedidosDTO.get(i)));
        }

        return pedidos;
    }

    public static ArrayList<OrdemPedido> findAllByDateWithStatusFromClient(PedidoFindJsonHelper dBH, Integer status){
        ArrayList<OrdemPedidoDTO> pedidosDTO = findAllDTOByDateWithStatusFromClient(dBH, status);
        ArrayList<OrdemPedido> pedidos = new ArrayList<>();
        for(int i = 0; i<pedidosDTO.size(); i++){
            pedidos.add(OrdemPedidoDTO.toOrdemPedido(pedidosDTO.get(i)));
        }

        return pedidos;
    }

    public static ArrayList<OrdemPedidoDTO> findAllDTOByDateFromClient(PedidoFindJsonHelper dBH){
        ArrayList<OrdemPedidoDTO> pedidosDTO = new ArrayList<>();
        Gson gson = new Gson();
        String dBHJson = gson.toJson(dBH);
        String output = DefaultRequests.postObject("/pedidos/buscaPorDataPorCliente", dBHJson);

        Gson gson2 = new Gson();

        Type userListType = new TypeToken<ArrayList<OrdemPedidoDTO>>() {
        }.getType();

        pedidosDTO = gson2.fromJson(output, userListType);


        return pedidosDTO;
    }

    public static ArrayList<OrdemPedidoDTO> findAllDTOByDateWithStatusFromClient(PedidoFindJsonHelper dBH, Integer status){
        ArrayList<OrdemPedidoDTO> pedidosDTO = new ArrayList<>();
        Gson gson = new Gson();
        String dBHJson = gson.toJson(dBH);
        String output = DefaultRequests.postObject("/pedidos/buscaPorDataComStatusPorCliente/" + status, dBHJson);

        Gson gson2 = new Gson();

        Type userListType = new TypeToken<ArrayList<OrdemPedidoDTO>>() {
        }.getType();

        pedidosDTO = gson2.fromJson(output, userListType);


        return pedidosDTO;
    }


    //CRUD
    public static OrdemPedido insert(OrdemPedido pedido){
            Gson gson = new Gson();
            String input = gson.toJson(pedido);
            System.out.println("Entrada do POST" + input);
            String output = DefaultRequests.postObject("/pedidos", input);
            System.out.println("Saida do POST" + output);
            Gson pedJson = new Gson();
            OrdemPedido ped = pedJson.fromJson(new String(output.getBytes()), OrdemPedido.class);
            return ped;
        }

        public static int update(OrdemPedido newObj){
            Gson gson = new Gson();
            String json = gson.toJson(newObj);

            return DefaultRequests.putObject("/pedidos", json);
        }

        public static int delete(Long id){
            int state;
                state = DefaultRequests.deleteObjectById(id, "/pedidos");
            return state;
        }



        public static List<OrdemPedido> findAllByMoreStatus(List<Integer> status){
            List<OrdemPedido> lista = new ArrayList<>();

            for(int i = 0; i < status.size(); i++){
                List<OrdemPedido> tempList;
                tempList = findAllByStatus(status.get(i));
                lista.addAll(tempList);
            }

            return lista;
        }

        public static List<OrdemPedido> findAllByDateWithMoreStatus(PedidoFindJsonHelper dBH, List<Integer> status){
        List<OrdemPedido> lista = new ArrayList<>();

        for(int i = 0; i < status.size(); i++){
            List<OrdemPedido> tempList;
            tempList = findAllByDateWithStatus(dBH,status.get(i));
            lista.addAll(tempList);
        }

        return lista;
    }

        public static List<OrdemPedido> findAllByDateWithMoreStatusFromClient(PedidoFindJsonHelper dBH, List<Integer> status){
        List<OrdemPedido> lista = new ArrayList<>();

        for(int i = 0; i < status.size(); i++){
            List<OrdemPedido> tempList;
            tempList = findAllByDateWithStatusFromClient(dBH,status.get(i));
            lista.addAll(tempList);
        }

        return lista;
    }

}
