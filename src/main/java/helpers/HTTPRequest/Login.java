package helpers.HTTPRequest;

import com.google.gson.Gson;
import models.Usuario;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

public class Login {

    private static HttpURLConnection conn;

    public static Usuario login (Usuario user){

        //Gera String concatenada com os dados de Login e posteriormente faz encode em Base64
        String stringConcCredencial = user.getUsername() + ":" + user.getPass();
        String base64ConcCredential = new String(Base64.getEncoder().encode(stringConcCredencial.getBytes()));

        Gson gson = new Gson();
        String userJson = gson.toJson(user);

        //Cria o Header HTTP de autenticacao e cria a requisicao HTTP
        String autorizacaoHeader = "BASIC " + base64ConcCredential;


        String url = DefaultRequests.getRootUrl();
        String output = "";

        try {
            System.out.println("String Concatenada: " + stringConcCredencial + " String Base64: " + base64ConcCredential + "/n" + "String Header: " + autorizacaoHeader);

            conn = (HttpURLConnection) new URL(url + "/login").openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", autorizacaoHeader);

            System.out.println(userJson);
            OutputStream os = conn.getOutputStream();
            os.write(userJson.getBytes());
            os.flush();

            switch (conn.getResponseCode()){
                case 200:
                    System.out.println("Solicitacao feita com Sucesso!");
                    break;
                case 502:
                    System.out.println("Houve um problema de conexao");
                    break;
                case 401:
                    System.out.println("Sem autenticacao");
                    break;
                default:
                    System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + url);
                    break;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));


            String line;
            while ((line = br.readLine()) != null) {
                output += line;
            }

            conn.disconnect();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gson.fromJson(new String(output.getBytes()), Usuario.class);
    }


}
