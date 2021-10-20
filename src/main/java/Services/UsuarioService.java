package Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import helpers.HTTPRequest.DefaultRequests;
import models.Usuario;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class UsuarioService {

    public static Usuario getById(Long id){
        Usuario usr = new Usuario();
        try {
            String json = DefaultRequests.getObject(id.toString(), "/usuarios");

            Gson gson = new Gson();
            usr = gson.fromJson(new String(json.getBytes()), Usuario.class);
        }catch (IOException e){
            e.printStackTrace();
        }
        return usr;
    }


    public static ArrayList<Usuario> findAll(){
        ArrayList<Usuario> Usuarios = new ArrayList<>();
        String json = DefaultRequests.getAll("/usuarios");

        Gson gson = new Gson();

        Type userListType = new TypeToken<ArrayList<Usuario>>(){}.getType();

        Usuarios = gson.fromJson(json, userListType);

        for(int i = 0; i<Usuarios.size(); i++){
            System.out.println(Usuarios.get(i).getUsername());
        }

        return Usuarios;
    }

    public static Usuario insert(Usuario Usuario){
        Gson gson = new Gson();
        String input = gson.toJson(Usuario);
        System.out.println("Entrada do POST" + input);
        String output = DefaultRequests.postObject("/usuarios", input);
        System.out.println("Saida do POST" + output);
        Gson usrJson = new Gson();
        Usuario usr = usrJson.fromJson(new String(output.getBytes()), Usuario.class);
        return usr;
    }

    public static int update( Usuario newObj){
        Gson gson = new Gson();
        String json = gson.toJson(newObj);

        return DefaultRequests.putObject("/usuarios", json);
    }

    public static int delete(Long id){
        int state;
        state = DefaultRequests.deleteObjectById(id, "/usuarios");
        return state;
    }
}
