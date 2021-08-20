package Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import helpers.HTTPRequest.DefaultRequests;
import models.Cliente;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ClienteService {

    public static Cliente getById(Long id){
        Cliente cli = new Cliente();
        try {
            String json = DefaultRequests.getObject(id, "/clientes");

            Gson gson = new Gson();
            cli = gson.fromJson(new String(json.getBytes()), Cliente.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return cli;
    }

    public static ArrayList<Cliente> findAll(){
        ArrayList<Cliente> clientes = new ArrayList<>();
        String json = DefaultRequests.getAll("/clientes");

        Gson gson = new Gson();

        Type userListType = new TypeToken<ArrayList<Cliente>>(){}.getType();

        clientes = gson.fromJson(json, userListType);

        for(int i = 0; i<clientes.size(); i++){
            System.out.println(clientes.get(i).getNome());
        }

        return clientes;
    }

    public static String insert(Cliente cliente){
        Gson gson = new Gson();
        String input = gson.toJson(cliente);
        System.out.println("Entrada do POST" + input);
        String output = DefaultRequests.postObject("/clientes", input);
        System.out.println("Saida do POST" + output);
        return output;
    }

    public static int update( Cliente newObj){
        Gson gson = new Gson();
        String json = gson.toJson(newObj);

        return DefaultRequests.putObject("/clientes", json);
    }

    public static int delete(Long id){
        int state;
        state = DefaultRequests.deleteObject(id, "/clientes");
        return state;
    }
}
