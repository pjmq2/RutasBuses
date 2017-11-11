import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Controller {

    private Set<StringPair> pairsSet;
    private PrintWriter ofile;
    String oldRoute = "";
    String newline;
    HttpURLConnection request;
    int counter;

    public Controller(){
        pairsSet = new HashSet<>();
        counter = 0;
        newline = System.getProperty("line.separator");
        try {
            ofile = new PrintWriter("Distritos.csv");
            ofile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void locate(String routeName, String latitud, String longitud) throws Exception {

        String sURL = "http://nominatim.openstreetmap.org/search/";
        sURL = sURL.concat(latitud);
        sURL = sURL.concat(",");
        sURL = sURL.concat(longitud);
        sURL = sURL.concat("?format=json");

        //http://nominatim.openstreetmap.org/search/9.902208,-84.073457?format=json

        //System.out.println(sURL);

        // Connect to the URL using java's native library
        URL url = new URL(sURL);
        request = (HttpURLConnection) url.openConnection();
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
        for(int i = 2; i < values.length-1 && !found; i++){
            if(values[i].contains("Cantón")) {
                districtIndex = i-1;
                found = true;
            }
        }
        if(routeName.equals(oldRoute)){
            pairsSet.clear();
            oldRoute = routeName;
        }
        String district = values[districtIndex].substring(1);
        boolean success = pairsSet.add(new StringPair(routeName, district));
        if(success){
            System.out.println(routeName + ", " + district);
            //ofile.println(routeName + "," + district);
            Writer output = new BufferedWriter(new FileWriter("Distritos.csv", true));  //clears file every time
            output.append(newline + routeName + "," + district);
            output.close();
        }
        request.disconnect();
        counter++;
        //TimeUnit.SECONDS.sleep(1);
        System.out.println(counter);
    }

    public void parser(){
        try {
            String fileName = "RutasGPS.csv";

            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String data = br.readLine();
            String[] items;

            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            while ((data = br.readLine()) != null){
                boolean repeat = true;
                while (repeat) {
                    try {
                        items = data.split(",");
                        locate(items[0], items[1], items[2]);
                        repeat = false;
                        //elapsedTime = (System.currentTimeMillis() - startTime)/1000;
                        //System.out.println(((10*60)-elapsedTime));
                    } catch (Exception e) {
                        e.printStackTrace();
                        elapsedTime = (System.currentTimeMillis() - startTime)/1000;
                        if(elapsedTime<600){
                            System.out.println("Límite de 10 mins alcanzado, esperar: " + ((10*60)-elapsedTime) + " segundos.");
                            TimeUnit.SECONDS.sleep((10*60)-elapsedTime);
                        }
                        startTime = System.currentTimeMillis();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        Controller controller = new Controller();
        try {
            controller.parser();
            //control.locate("Ruta 1", "9.917", "-84.04054");
            //control.locate("Ruta 2", "9.917", "-84.04054");
            //control.ofile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class StringPair {
        String first;
        String second;
        public StringPair(String first, String second){
            this.first = first;
            this.second = second;
        }
        public boolean equals(Object o) {
            if (o instanceof StringPair) {
                StringPair sp = (StringPair)o;
                return sp.first.equals(first) && sp.second.equals(second);
            }
            return false;
        }
        public int hashCode() {
            return new String(first).hashCode() * 31 + new String(second).hashCode();
        }
    }
}