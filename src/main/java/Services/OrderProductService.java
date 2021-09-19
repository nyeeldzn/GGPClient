package Services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import helpers.HTTPRequest.DefaultRequests;
import models.OrderProduct;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class OrderProductService {
    private static HttpURLConnection conn;


    public static OrderProduct getById(Long id){
            OrderProduct cli = new OrderProduct();
            try {
                String json = DefaultRequests.getObject(id.toString(), "/pedidos/orderproduct");

                Gson gson = new Gson();
                cli = gson.fromJson(new String(json.getBytes()), OrderProduct.class);
            }catch (IOException e){
                e.printStackTrace();
            }
            return cli;
        }


    public static ArrayList<OrderProduct> findAll(){
            ArrayList<OrderProduct> produtos = new ArrayList<>();
            String json = DefaultRequests.getAll("/pedidos/orderproduct");

            Gson gson = new Gson();

            Type userListType = new TypeToken<ArrayList<OrderProduct>>(){}.getType();

            produtos = gson.fromJson(json, userListType);

            return produtos;
        }

        public static String insert(OrderProduct produto){
            Gson gson = new Gson();
            String input = gson.toJson(produto);
            System.out.println("Entrada do POST" + input);
            String output = DefaultRequests.postObject("/pedidos/orderproduct", input);
            System.out.println("Saida do POST" + output);
            return output;
        }

        public static int delete(Long id){
            int state;
                state = DefaultRequests.deleteObjectById(id, "/pedidos/orderproduct");
            return state;
        }

    public static int deleteObjectByObject(OrderProduct order) {
        int state = 0;

        conn = DefaultRequests.getConnection();
        String rootUrl = DefaultRequests.getRootUrl();

        Gson gson = new Gson();
        String input = gson.toJson(order);
        System.out.println("Entrada do DELETE" + input);

        try {

            conn = (HttpURLConnection) new URL(rootUrl + "/pedidos/orderproduct").openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");

            System.out.println(input);
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            System.out.println("Output from Server .... \n" + br.readLine());

            conn.disconnect();

        } catch (ProtocolException protocolException) {
            protocolException.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            switch (conn.getResponseCode()) {
                case HttpURLConnection.HTTP_OK:
                    state = 2;
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    state = 1;
                    break;
                default:
                    state = 0;
                    System.out.println("Codigo de erro resultante: " + conn.getResponseCode());
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return state;
    }

}
