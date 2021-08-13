package helpers.HTTPRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DefaultRequests {

    private static HttpURLConnection conn;
    private static String rootUrl;

    public static void setConnect(String url) throws IOException {
        rootUrl = url;
    }

    public static String getObject(Long id, String node) throws IOException {

        conn = (HttpURLConnection) new URL(rootUrl + node + "/" + id).openConnection();

        String output = "";
        try {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

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

    public static String getAll(String node){
        String output = "";
        try {
            conn = (HttpURLConnection) new URL(rootUrl + node).openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

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

    public static void postObject(){}

    public static void putObject(){}

    public static void deleteObject(){}
}
