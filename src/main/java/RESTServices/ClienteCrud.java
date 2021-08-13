package RESTServices;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


public class ClienteCrud {

    public static boolean testeGet() {
        boolean state= false;
        //Item unico
        /*try {
            String url = "http://localhost:8081/clientes/1";

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                state = false;
                System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
            }else{
                state = true;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = "";
            String line;
            while ((line = br.readLine()) != null) {
                output += line;
            }

            conn.disconnect();


            Gson gson = new Gson();
            Cliente dados = gson.fromJson(new String(output.getBytes()), Cliente.class);

            System.out.println("TIME: " + dados.getNome());
            System.out.println("STATES: " + dados.getEndereco());


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */

        //Lista de Itens
        try {
            String url = "http://localhost:8081/clientes";

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                state = false;
                System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
            }else{
                state = true;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output = "";
            String line;
            while ((line = br.readLine()) != null) {
                output += line;
            }

            conn.disconnect();

            Gson gson = new Gson();

            Type userListType = new TypeToken<ArrayList<Cliente>>(){}.getType();

            ArrayList<Cliente> lista = gson.fromJson(output, userListType);

            for(int i = 0; i<lista.size(); i++){
                System.out.println(lista.get(i).getNome());
            }


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    return state;
    }
}
