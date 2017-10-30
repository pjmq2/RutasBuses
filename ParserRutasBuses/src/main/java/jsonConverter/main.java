package jsonConverter;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {
    public static void main(String[] args){
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
