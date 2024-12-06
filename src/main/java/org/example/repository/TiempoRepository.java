package org.example.repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.example.model.Tiempo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TiempoRepository {

    //Api 
    private  static final String API_URL = "https://www.meteogalicia.gal/web/rss-georss-json";
    private static final String API_KEY ="MWXPF8V59rh5BX8G983Bj4aWCm08aalDegD98fIKni4C2lK5jJnMIhA11lbde1MF";

        //Metodo para conseguir el Tiempo
        public Tiempo getTiempo(String location) throws Exception{

            //construccion de la url
            String urlString = API_URL + "?location=" + location + "&API_KEY=" + API_KEY;
            URL url = new URL(urlString);
            
            //Configuración de la conexión
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x-api-key", API_KEY);

            //Vemos la respuesta que conseguimos 
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
           //Leemos la respuesta y constuimos el objeto json
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            //Cerramos la conexion
            br.close();
            //Parseamos el Json que llega
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());
            

            //Cogemos los datos que queremos 
            String estadoCielo = root.path("estado_cielo").asText();
            String temperatura = root.path("temperatura").asText();
            String viento = root.path("viento").asText();
            String humedad = root.path("humedad").asText();
            String coberturaNubosa = root.path("cobertura_nubosa").asText();

            //Creamos el objeto Tiempo
            Tiempo tiempo = new Tiempo(estadoCielo, temperatura, viento, humedad, coberturaNubosa);
            
            return tiempo;
        }

        
 }
    
