package jsonConverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class main{
    public static void main(String[] args){
        try {
            otro("9.902208","-84.073457");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // https://geocode.xyz/9.902208,-84.073457?geoit=json
        //http://nominatim.openstreetmap.org/search/9.902208,-84.073457?format=json
    }

    public static void otro(String latitud, String longitud) throws IOException {

        String sURL = "http://nominatim.openstreetmap.org/search/";
        sURL = sURL.concat(latitud);
        sURL = sURL.concat(",");
        sURL = sURL.concat(longitud);
        sURL = sURL.concat("?format=json");

        System.out.println(sURL);

        // Connect to the URL using java's native library
        URL url = new URL(sURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        request.connect();

        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        System.out.print(request.getContent());
        //JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
        //String distrito = rootobj.get("city").getAsString();
        //System.out.println(root.toString());
        //System.out.println(distrito);
    }

    public void parser(){
        String fileName = "Paradas de Buses.csv";
        File file = new File(fileName);
        try {
            Scanner inputStream = new Scanner(file);
            int limitador = 0;
            int columna = 0;
            while(inputStream.hasNext() && limitador<700 ){
                String data = inputStream.next();
                if(data.contains("\",\"")){
                    String[] valores = data.split("\",\"");
                    System.out.print(valores[0]);
                    System.out.println();
                    System.out.println();
                    System.out.print(valores[1]);
                }
                else{
                    System.out.print(data);
                }
                limitador ++;
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}