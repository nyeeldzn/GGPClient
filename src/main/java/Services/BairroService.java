package Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import helpers.HTTPRequest.DefaultRequests;
import models.Bairro;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class BairroService {

    public static Bairro getById(Long id){
        Bairro bairro = new Bairro();
        try {
            String json = DefaultRequests.getObject(id.toString(), "/clientes");

            Gson gson = new Gson();
            bairro = gson.fromJson(new String(json.getBytes()), Bairro.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return bairro;
    }

    public static ArrayList<Bairro> findAll(){
        ArrayList<Bairro> bairros = new ArrayList<>();
        String json = DefaultRequests.getAll("/bairros");

        Gson gson = new Gson();

        Type userListType = new TypeToken<ArrayList<Bairro>>(){}.getType();

        bairros = gson.fromJson(json, userListType);

        for(int i = 0; i<bairros.size(); i++){
            System.out.println(bairros.get(i).getNome());
        }

        return bairros;
    }

    public static String insert(Bairro bairro){
        Gson gson = new Gson();
        String input = gson.toJson(bairro);
        System.out.println("Entrada do POST" + input);
        String output = DefaultRequests.postObject("/bairros", input);
        System.out.println("Saida do POST" + output);
        return output;
    }

    public static int update( Bairro newObj){
        Gson gson = new Gson();
        String json = gson.toJson(newObj);

        return DefaultRequests.putObject("/bairros", json);
    }

    public static int delete(Long id){
        int state;
        state = DefaultRequests.deleteObject(id, "/bairros");
        return state;
    }
}
