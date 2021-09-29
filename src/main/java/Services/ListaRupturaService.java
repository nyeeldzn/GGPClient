package Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import helpers.HTTPRequest.DefaultRequests;
import models.ListaRuptura;
import models.PedidoFindJsonHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListaRupturaService {

        public static ListaRuptura getById(Long id){
            ListaRuptura ped = new ListaRuptura();
            try {
                String json = DefaultRequests.getObject(id.toString(), "/listaRuptura");

                Gson gson = new Gson();
                ped = gson.fromJson(new String(json.getBytes()), ListaRuptura.class);
            }catch (IOException e){
                e.printStackTrace();
            }
            return ped;
        }


        public static ArrayList<ListaRuptura> findAll(){
            ArrayList<ListaRuptura> pedidosDTO = new ArrayList<>();
            String json = DefaultRequests.getAll("/listaRuptura");

            Gson gson = new Gson();

            Type userListType = new TypeToken<ArrayList<ListaRuptura>>(){}.getType();

            pedidosDTO = gson.fromJson(json, userListType);


            return pedidosDTO;
        }

    public static ArrayList<ListaRuptura> findAllByDate(PedidoFindJsonHelper dBH){
        ArrayList<ListaRuptura> pedidosDTO = new ArrayList<>();
        Gson gson = new Gson();
        String dBHJson = gson.toJson(dBH);
        String output = DefaultRequests.postObject("/listaRuptura/buscaPorData", dBHJson);

        Gson gson2 = new Gson();

        Type userListType = new TypeToken<ArrayList<ListaRuptura>>() {
        }.getType();

        pedidosDTO = gson2.fromJson(output, userListType);



        return pedidosDTO;
    }

    //CRUD
    public static ListaRuptura insert(ListaRuptura pedido){
            Gson gson = new Gson();
            String input = gson.toJson(pedido);
            System.out.println("Entrada do POST" + input);
            String output = DefaultRequests.postObject("/listaRuptura", input);
            System.out.println("Saida do POST" + output);
            Gson pedJson = new Gson();
            ListaRuptura ped = pedJson.fromJson(new String(output.getBytes()), ListaRuptura.class);
            return ped;
        }

        public static int update(ListaRuptura newObj){
            Gson gson = new Gson();
            String json = gson.toJson(newObj);

            return DefaultRequests.putObject("/listaRuptura", json);
        }

        public static int delete(Long id){
            int state;
                state = DefaultRequests.deleteObjectById(id, "/listaRuptura");
            return state;
        }

}
