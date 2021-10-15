package helpers.HTTPRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DefaultRequests {

    private static HttpURLConnection conn;
    private static String rootUrl;
    private static String authHeader;

    public static void setConnect(String url) throws IOException {
        rootUrl = url;
    }

    public static void setAuthHeader(String header){
        authHeader = header;
    }

    public static HttpURLConnection getConnection(){
        return conn;
    }

    public static String getRootUrl(){
        return rootUrl;
    }

    public static String getObject(String variable, String node) throws IOException {

        conn = (HttpURLConnection) new URL(rootUrl + node + "/" + variable).openConnection();

        String output = "";
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeader);

            if (conn.getResponseCode() != 200) {
                System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + rootUrl);
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

        return output;
    }

    public static String getAll(String node){
        String output = "";
        try {
            conn = (HttpURLConnection) new URL(rootUrl + node).openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", authHeader);

            if (conn.getResponseCode() != 200) {
                System.out.println("Erro " + conn.getResponseCode() + " ao obter dados da URL " + rootUrl);
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
        System.out.println("JSON de SAIDA" + output);
        return output;
    }

    public static String getObjectWithBody(String node, String json){
        String output = null;
        try {

            conn = (HttpURLConnection) new URL(rootUrl + node).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", authHeader);

            System.out.println(json);
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));


            System.out.println("Output from Server .... \n");

            output = br.readLine();


            conn.disconnect();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }


    public static String postObject(String node, String json){
        String output = null;
        try {

            conn = (HttpURLConnection) new URL(rootUrl + node).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", authHeader);

            System.out.println(json);
            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));


            System.out.println("Output from Server .... \n");
            /*
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

             */

            output = br.readLine();


            conn.disconnect();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static int deleteObjectById(Long id, String node) {
        int state = 0;

        try {
            conn = (HttpURLConnection) new URL(rootUrl + node + "/" + id).openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", authHeader);
            conn.connect();



        }catch (ProtocolException protocolException){
            protocolException.printStackTrace();
        }catch (IOException e){
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


    public static int putObject(String node, String json){
        int state = 0;
        try {
            conn = (HttpURLConnection) new URL(rootUrl + node ).openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty(
                    "Content-Type", "application/json");
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Authorization", authHeader);

            OutputStreamWriter out = new OutputStreamWriter(
                    conn.getOutputStream());
            out.write(json);
            out.close();
            conn.getInputStream();

        }catch (IOException e){
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
                    System.out.println("Codigo de erro resultante: " + conn.getResponseCode() + "Com json: " + json);
                    break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return state;
    }

}
