import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Controllator {


    public void init() {
        //parserInfo();
        parserGPS();
    }

    public void parserInfo() {
        String fileName = "Paradas de Buses.csv";
        File file = new File(fileName);

        try {
            Scanner inputStream = new Scanner(file).useDelimiter("\n");
            PrintWriter ofile = new PrintWriter("RutasInfo.csv");

            ofile.println("Ruta,Recorrido,Distancia,Empresa");


            int dataIndex = 0;

            int limitador = 0;
            int columna = 0;
            while (inputStream.hasNext()) {

                String ruta = "";
                String recorrido  = "";
                String distancia  = "";
                String numRutas  = "";
                String empresa  = "";

                String data = inputStream.nextLine();


                if (!data.isEmpty()) {
                    int htmlBegin = data.indexOf('<');
                    int htmlEnd = data.lastIndexOf('>') + 1;
                    int rutaEnd = data.indexOf(',');


                    ruta = data.substring(0, rutaEnd);
                    ruta = ruta.replace("\"", "");
                    //System.out.println(ruta);

                    String html = data.substring(htmlBegin, htmlEnd);
                    int recorridoBegin = html.indexOf("recorrido</td><td>") + "recorrido</td><td>".length();
                    int recorridoEnd = html.indexOf("</td>", recorridoBegin);
                    recorrido = (recorridoBegin >= recorridoEnd) ? "NULL" : html.substring(recorridoBegin, recorridoEnd);

                    //System.out.println(recorrido);

                    int distanciaBegin = html.indexOf("Length</td><td>") + "Length</td><td>".length();
                    int distanciaEnd = html.indexOf("</td>", distanciaBegin);
                    distancia = (distanciaBegin >= distanciaEnd) ? "NULL" : html.substring(distanciaBegin, distanciaEnd);
                    distancia = distancia.replace(",",".");
                    //System.out.println(distancia);

                    int numRutaBegin = html.indexOf("ruta</td><td>") + "ruta</td><td>".length();
                    int numRutaEnd = html.indexOf("</td>", numRutaBegin);
                    numRutas = (numRutaBegin >= numRutaEnd) ? "NULL" : html.substring(numRutaBegin, numRutaEnd);
                    //System.out.println(numRutas);


                    int empresaBegin = numRutas.indexOf("(") + "(".length();
                    int empresaEnd = numRutas.indexOf(")",empresaBegin);
                    empresa =(empresaBegin>=empresaEnd)? "NULL" : numRutas.substring(empresaBegin,empresaEnd);
                    //System.out.println(empresa);

                    ofile.println(ruta+","+recorrido+","+distancia+","+empresa);
                    limitador++;
                    if(numRutas == "NULL") {
                        System.out.println(data);
                    }
                }
            }
            inputStream.close();
            ofile.close();
            System.out.println(limitador);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void parserGPS() {
        String fileName = "Paradas de Buses.csv";
        File file = new File(fileName);

        try {
            Scanner inputStream = new Scanner(file).useDelimiter("\n");
            PrintWriter ofile = new PrintWriter("RutasGPS.csv");

            ofile.println("Recorrido,Latitud,Longitud");
            while (inputStream.hasNext()) {

                String ruta = "";
                String latitud = "";
                String longitud = "";
                String allGPS = "";
                String data = inputStream.nextLine();


                if (!data.isEmpty()) {

                    int recorridoBegin = data.indexOf("recorrido</td><td>") + "recorrido</td><td>".length();
                    int recorridoEnd = data.indexOf("</td>", recorridoBegin);
                    ruta = (recorridoBegin >= recorridoEnd) ? "NULL" : data.substring(recorridoBegin, recorridoEnd);

                    int gpsBegin = data.lastIndexOf('>') + 4;
                    int gpsEnd = data.lastIndexOf(",")-2;
                    allGPS = (gpsBegin >= gpsEnd) ? "NULL" : data.substring(gpsBegin, gpsEnd);

                    System.out.println(allGPS);


                        String[] datos = allGPS.split(" ");


                    for (int i = 0 ; i< datos.length ; i++) {

                        String[] tripleta = datos[i].split(",");
                        latitud= tripleta[0];
                        longitud = tripleta[1];

                        ofile.println(ruta + "," +latitud + "," + longitud);
                    }


                }
            }

        }catch (Exception e) {

        }

    }


    public static void main(String[] args) {
        Controllator controllator = new Controllator();
        controllator.init();
    }



}
