package Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import helpers.HTTPRequest.DefaultRequests;
import models.Produto;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ProdutoService {

        public static Produto getById(Long id){
            Produto cli = new Produto();
            try {
                String json = DefaultRequests.getObject(id.toString(), "/clientes");

                Gson gson = new Gson();
                cli = gson.fromJson(new String(json.getBytes()), Produto.class);
            }catch (IOException e){
                e.printStackTrace();
            }
            return cli;
        }

    public static ArrayList<Produto> getByNome(String nome){
        ArrayList<Produto> produtos = new ArrayList<>();

        Produto produto = new Produto();
        produto.setId(null);
        produto.setNome(nome);
        Gson gson = new Gson();
        String input = gson.toJson(produto);

        String json = DefaultRequests.getObjectWithBody("/produtos/buscaPorNome", input);

        Gson returnJson = new Gson();

        Type userListType = new TypeToken<ArrayList<Produto>>(){}.getType();

        produtos = returnJson.fromJson(json, userListType);

        for(int i = 0; i<produtos.size(); i++){
            System.out.println(produtos.get(i).getNome());
        }
        return produtos;
    }


    public static ArrayList<Produto> findAll(){
            ArrayList<Produto> produtos = new ArrayList<>();
            String json = DefaultRequests.getAll("/produtos");

            Gson gson = new Gson();

            Type userListType = new TypeToken<ArrayList<Produto>>(){}.getType();

            produtos = gson.fromJson(json, userListType);

            for(int i = 0; i<produtos.size(); i++){
                System.out.println(produtos.get(i).getNome());
            }

            return produtos;
        }

        public static Produto insert(Produto produto){
            Gson gson = new Gson();
            String input = gson.toJson(produto);
            System.out.println("Entrada do POST" + input);
            String output = DefaultRequests.postObject("/produtos", input);
            System.out.println("Saida do POST" + output);

            Produto prod = gson.fromJson(new String(output.getBytes()), Produto.class);

            return prod;
        }

        public static int update( Produto newObj){
            Gson gson = new Gson();
            String json = gson.toJson(newObj);

            return DefaultRequests.putObject("/produtos", json);
        }

        public static int delete(Long id){
            int state;
                state = DefaultRequests.deleteObjectById(id, "/produtos");
            return state;
        }
    }
