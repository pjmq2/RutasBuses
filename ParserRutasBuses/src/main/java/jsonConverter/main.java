package jsonConverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.rmi.rmid.ExecOptionPermission;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class main {
    public static void main(String[] args) {
        try {
            locate("9.917", "-84.04054");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // https://geocode.xyz/9.902208,-84.073457?geoit=json
        //http://nominatim.openstreetmap.org/search/9.902208,-84.073457?format=json
    }

    public static void locate(String latitud, String longitud) throws Exception {

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

        String incorrectJson = root.toString();
        //System.out.println(root.toString());
        int importanceIndex = incorrectJson.indexOf("importance");
        incorrectJson = incorrectJson.substring(0, importanceIndex + 12) + "\"" + incorrectJson.substring(importanceIndex + 12);
        incorrectJson = incorrectJson.substring(0, importanceIndex + 18) + "\"" + incorrectJson.substring(importanceIndex + 18);
        incorrectJson = incorrectJson.substring(1, incorrectJson.length()-1);
        System.out.println(incorrectJson);
        root = jp.parse(incorrectJson);

        JsonObject rootobj = root.getAsJsonObject(); //May be an array, may be an object.
        String placeName = rootobj.get("display_name").getAsString();
        System.out.println(root.toString());
        //System.out.println(placeName);

        String[] values;
        //placeName = placeName.replaceAll("\\s+","");
        values = placeName.split(",");
        values[3] = values[3].substring(1, values[3].length());

        System.out.println(values[3]);
    }

    public static void parser(){
        try {
            String fileName = "RutasGPS.csv";

            BufferedReader br = new BufferedReader(new FileReader(fileName));

            PrintWriter ofile = new PrintWriter(fileName);
            ofile.println("Ruta,Recorrido,Distancia,Empresa");

            String data = br.readLine();
            String[] items;

            while ((data = br.readLine()) != null){
                boolean repeat = true;
                while (repeat) {
                    try {
                        items = data.split(",");
                        //locate(items[1], items[2]);
                        repeat = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}