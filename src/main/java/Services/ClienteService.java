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

        ArrayList<Cliente> lista = gson.fromJson(json, userListType);

        for(int i = 0; i<lista.size(); i++){
            System.out.println(lista.get(i).getNome());
        }

        return clientes;
    }

}
