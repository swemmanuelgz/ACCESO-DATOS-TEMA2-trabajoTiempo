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
    private  static final String API_URL = "https://servizos.meteogalicia.gal/mix/rest/forecast/consulta";
    private static final String API_KEY ="MWXPF8V59rh5BX8G983Bj4aWCm08aalDegD98fIKni4C2lK5jJnMIhA11lbde1MF";

        //Metodo para conseguir el Tiempo
        public Tiempo getTiempo(String location) throws Exception {
            // Endpoint para buscar el ID de la localidad
            String findPlaceURL = "https://servizos.meteogalicia.gal/apiv4/findPlaces?name=" + location;
            URL findPlaceEndpoint = new URL(findPlaceURL);
        
            // Conexión para obtener el ID de la localidad
            HttpURLConnection findPlaceConnection = (HttpURLConnection) findPlaceEndpoint.openConnection();
            findPlaceConnection.setRequestMethod("GET");
            findPlaceConnection.setRequestProperty("x-api-key", API_KEY);
        
            if (findPlaceConnection.getResponseCode() != 200) {
                throw new Exception("Error al buscar ID de la localidad: " + findPlaceConnection.getResponseCode());
            }
        
            // Leer respuesta para obtener ID de la localidad
            BufferedReader brFindPlace = new BufferedReader(new InputStreamReader(findPlaceConnection.getInputStream()));
            StringBuilder findPlaceResponse = new StringBuilder();
            String line;
            while ((line = brFindPlace.readLine()) != null) {
                findPlaceResponse.append(line);
            }
            brFindPlace.close();
        
            // Parsear JSON de búsqueda de localidad
            ObjectMapper mapper = new ObjectMapper();
            JsonNode findPlaceRoot = mapper.readTree(findPlaceResponse.toString());
            String locationID = findPlaceRoot.path("id").asText(); // Ajustar clave según respuesta
        
            if (locationID.isEmpty()) {
                throw new Exception("No se encontró la localidad: " + location);
            }
        
            // Endpoint para obtener el tiempo usando el ID de la localidad
            String weatherURL = "https://servizos.meteogalicia.gal/mix/rest/forecast/consulta?locationID=" + locationID;
            URL weatherEndpoint = new URL(weatherURL);
        
            HttpURLConnection weatherConnection = (HttpURLConnection) weatherEndpoint.openConnection();
            weatherConnection.setRequestMethod("GET");
            weatherConnection.setRequestProperty("x-api-key", API_KEY);
        
            if (weatherConnection.getResponseCode() != 200) {
                throw new Exception("Error al conseguir el tiempo: " + weatherConnection.getResponseCode());
            }
        
            // Leer respuesta de la API del tiempo
            BufferedReader brWeather = new BufferedReader(new InputStreamReader(weatherConnection.getInputStream()));
            StringBuilder weatherResponse = new StringBuilder();
            while ((line = brWeather.readLine()) != null) {
                weatherResponse.append(line);
            }
            brWeather.close();
        
            // Depurar el JSON recibido
            System.out.println("JSON del tiempo: " + weatherResponse.toString());
        
            // Parsear el JSON del tiempo
            JsonNode weatherRoot = mapper.readTree(weatherResponse.toString());
        
            // Ajustar las claves según la respuesta real de la API
            String estadoCielo = weatherRoot.path("sky_state").asText(); // Ejemplo
            String temperatura = weatherRoot.path("temperature").asText(); // Ejemplo
            String viento = weatherRoot.path("wind_speed").asText(); // Ejemplo
            String humedad = weatherRoot.path("humidity").asText(); // Ejemplo
            String coberturaNubosa = weatherRoot.path("cloud_cover").asText(); // Ejemplo
        
            // Crear y devolver el objeto Tiempo
            return new Tiempo(estadoCielo, temperatura, viento, humedad, coberturaNubosa);
        }
        

        
 }
    
