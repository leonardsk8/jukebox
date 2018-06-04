package ilioncorp.com.jukebox.utils.services;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;


import ilioncorp.com.jukebox.utils.constantes.ERoutes;


/**
 * Clase que realiza la petición
 * Created by lrey on 3/8/18.
 */

public class Servicio {

    private static final String DOMINIO = "http://10.255.14.201:8080/autos/webresources/android/autos";

    public static void invocar(final ERoutes ruta, final Object parametros, final Handler puente) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                Message mensaje = new Message();
                try {
                    URL direccion = new URL(DOMINIO + ruta.getUrl());
                    HttpURLConnection cnn = (HttpURLConnection) direccion.openConnection();
                    cnn.setRequestMethod("POST");
                    cnn.setDoOutput(true);
                    cnn.setDoInput(true);
                    cnn.setRequestProperty("Content-Type", "application/json");
                    PrintStream out = new PrintStream(cnn.getOutputStream());
                    out.print(new Gson().toJson(parametros));
                    out.close();
                    BufferedReader lector = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
                    String linea = lector.readLine();
                    String contenido = "";
                    while (linea != null) {
                        contenido += linea;
                        linea = lector.readLine();
                    }
                    lector.close();
                    Properties propiedades = new Properties();
                    propiedades.put("url", ruta.getUrl());
                    propiedades.put("contenido", contenido);
                    mensaje.obj = propiedades;
                    //Notifica a la vista de la respuesta
                    puente.sendMessage(mensaje);
                } catch (Exception e) {
                    e.printStackTrace();
                    mensaje.obj = e.getMessage();
                    //Notifica a la vista de un error
                    puente.sendMessage(mensaje);
                }
            }
        });
        hilo.start();

    }

    public void invocarRest(ERoutes ruta, Object parametros, Handler puente) {
        try {
            URL direccion = new URL(DOMINIO + ruta.getUrl());
            HttpURLConnection cnn = (HttpURLConnection) direccion.openConnection();
            cnn.setRequestMethod("GET");
            //cnn.setDoOutput(true);
            cnn.setDoInput(true);
            /*PrintStream out = new PrintStream(cnn.getOutputStream());
            out.print("usuario=" + new Gson().toJson(parametros) + "&otro=123");
            out.close();*/
            BufferedReader lector = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
            String linea = lector.readLine();
            String contenido = "";
            while (linea != null) {
                contenido += linea;
                linea = lector.readLine();
            }
            lector.close();
            Message mensaje = new Message();
            mensaje.obj = contenido;
            //Notifica a la vista de la respuesta
            puente.sendMessage(mensaje);
        } catch (Exception e) {
            Message mensaje = new Message();
            mensaje.obj = e.getMessage();
            //Notifica a la vista de un error
            puente.sendMessage(mensaje);
        }
    }

    public static void subirImagen() {
        Log.d("Process", "uploading...");
        try {
            String sourceFileUri = Environment.getExternalStorageDirectory() + "/toronto_noche.png";
            Log.d("File", sourceFileUri);
            HttpURLConnection conn = null;
            DataOutputStream dos = null;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(sourceFileUri);
            if (sourceFile.isFile()) {
                //return;
            }
            String upLoadServerUri = "http://192.168.2.104:8080/Rest/webresources/productos/subirarchivo";
            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upLoadServerUri);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"usuario\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.write("Leonardo Rey Baquero".getBytes());
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"archivo\";filename=\"" + sourceFileUri + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math
                        .min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0,
                        bufferSize);

            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            fileInputStream.close();
            dos.flush();
            dos.close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = bufferedReader.readLine();
            Log.d("response", "" + line);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
