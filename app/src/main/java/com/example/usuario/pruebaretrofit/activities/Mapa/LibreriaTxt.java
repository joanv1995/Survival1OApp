package com.example.usuario.pruebaretrofit.activities.Mapa;

import android.nfc.Tag;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class LibreriaTxt {


    public static void pasarMapaTxt(String[][] mapa, String nomTxt){
        final String TAG = "LlibreriaTxt";
        String savePath=" ";
        try {
            savePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "mapesTxt";
            //File saveLocation = new File(savePath);
            File myFile = new File(savePath, nomTxt +".txt");
            FileWriter writer = new FileWriter(myFile, false);
            for (String[] p: mapa) {
                for (String pp:p) {
                    writer.write(pp);
                }
                writer.write("\r\n");
            }
            writer.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            //log.info("No se ha podido crear el fichero del mapa: "+nomTxt);
        }
        Log.d(TAG,"Se ha creado el mapa: "+nomTxt+". Se ha guardado en: "+savePath);
    }

    public static String[][] llegirMapaTxt(String nomTxt){
        final String TAG = "LlibreriaTxt";
        String line="";
        int cont=1;
        String savePath = System.getProperty("user.dir") + System.getProperty("file.separator") + "assets";
        File myFile = new File(savePath, nomTxt+".txt");
        try {
            // per saber la mida del mapa (amplada i altura)
            FileReader reader = new FileReader(myFile);
            BufferedReader bufferedReader = new BufferedReader(reader);

            line = bufferedReader.readLine();
            while (bufferedReader.readLine() != null){
                cont++;
            }
            String[][] malla = new String [cont][line.length()];
            reader.close();
        } catch (Exception e){
            Log.e(TAG,e.getMessage());
            //log.info("No se ha podido cargar el mapa: "+nomTxt+".txt");
            return null;
        }
        String[][] malla = new String [cont][line.length()];
        try{
            // Per omplir la malla
            FileReader reader = new FileReader(myFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            cont=0;
            while ((line = bufferedReader.readLine()) != null){
                for (int i=0; i<line.length(); i++) {
                    malla[cont][i] = String.valueOf(line.charAt(i));
                }
                cont++;
            }
            reader.close();

        } catch (Exception e){
            Log.e(TAG,"No entenc què està passant.");
            return null;
        }
        //String[][] malla = String [][];
        return malla;

    }

}
