package Services;

import DTO.OrdemPedidoDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import helpers.HTTPRequest.DefaultRequests;
import models.OrdemPedido;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class PedidoService {

        public static OrdemPedido getById(Long id){
            OrdemPedido ped = new OrdemPedido();
            try {
                String json = DefaultRequests.getObject(id.toString(), "/clientes");

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
        public static ArrayList<OrdemPedidoDTO> findAllDTO(){
            ArrayList<OrdemPedidoDTO> pedidosDTO = new ArrayList<>();
            String json = DefaultRequests.getAll("/pedidos");

            Gson gson = new Gson();

            Type userListType = new TypeToken<ArrayList<OrdemPedidoDTO>>(){}.getType();

            pedidosDTO = gson.fromJson(json, userListType);

            for(int i = 0; i<pedidosDTO.size(); i++){
                System.out.println("Pedido de id: " + pedidosDTO.get(i).getId() + " recuperado." );
            }

            return pedidosDTO;
        }

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
                state = DefaultRequests.deleteObject(id, "/pedidos");
            return state;
        }
    }
