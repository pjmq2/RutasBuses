import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static Set<pair> pairsSet = new HashSet<>();

    public static class pair{
        String first;
        String second;
        private pair(String first, String second){
            this.first = first;
            this.second = second;
        }
        public boolean equals(Object o) {
            if (o instanceof pair) {
                pair p = (pair)o;
                return p.first.equals(first) && p.second.equals(second);
            }
            return false;
        }
        public int hashCode() {
            return new String(first).hashCode() * 31 + new String(second).hashCode();
        }
    }

    public static void main(String[] args) {
        try {
            parser();
            //locate("Holi", "9.917", "-84.04054");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //http://nominatim.openstreetmap.org/search/9.902208,-84.073457?format=json
    }

    public static void locate(String routeName, String latitud, String longitud) throws Exception {

        String sURL = "http://nominatim.openstreetmap.org/search/";
        sURL = sURL.concat(latitud);
        sURL = sURL.concat(",");
        sURL = sURL.concat(longitud);
        sURL = sURL.concat("?format=json");

        //System.out.println(sURL);

        // Connect to the URL using java's native library
        URL url = new URL(sURL);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        request.connect();

        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element

        String location = root.toString();
        int index1 = location.indexOf("display_name");
        location = location.substring(index1+15);
        int index2 = location.indexOf("\"");
        location = location.substring(0, index2);
        //System.out.println(location);

        String[] values;
        values = location.split(",");
        boolean found = false;
        int districtIndex = 0;
        for(int i = 1; i < values.length-1 && !found; i++){
            if(values[i].contains("Cantón")) {
                districtIndex = i-1;
                found = true;
            }
        }

        String district = values[districtIndex].substring(1);
        boolean success = pairsSet.add(new pair(routeName, district));
        //if(success){
            System.out.println(routeName + ", " + district);
        //}
    }

    public static void parser(){
        try {
            String fileName = "RutasGPS.csv";

            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String data = br.readLine();
            String[] items;

            while ((data = br.readLine()) != null){
                boolean repeat = true;
                while (repeat) {
                    try {
                        items = data.split(",");
                        locate(items[0], items[1], items[2]);
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