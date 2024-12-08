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
    private static final String FIND_PLACE_URL = "https://servizos.meteogalicia.gal/apiv4/findPlaces?location=";
    private static final String WEATHER_URL = "https://servizos.meteogalicia.gal/apiv4/getNumericForecastInfo";
    private static final String API_KEY = "MWXPF8V59rh5BX8G983Bj4aWCm08aalDegD98fIKni4C2lK5jJnMIhA11lbde1MF";

        //Metodo para conseguir el Tiempo
       
public Tiempo getTiempo(String location) throws Exception {
    // URL de la API que ya tienes definida con coordenadas
    String weatherURL = "https://servizos.meteogalicia.gal/apiv4/getNumericForecastInfo?coords=-8.18142,43.46164&variables=temperature,wind,sky_state,relative_humidity,cloud_area_fraction&API_KEY=" + API_KEY;
    URL weatherEndpoint = new URL(weatherURL);

    // Conexión HTTP
    HttpURLConnection weatherConnection = (HttpURLConnection) weatherEndpoint.openConnection();
    weatherConnection.setRequestMethod("GET");
    weatherConnection.setRequestProperty("x-api-key", API_KEY);

    if (weatherConnection.getResponseCode() != 200) {
        throw new Exception("Error al conseguir el tiempo: " + weatherConnection.getResponseCode());
    }

    // Leer respuesta
    BufferedReader brWeather = new BufferedReader(new InputStreamReader(weatherConnection.getInputStream()));
    StringBuilder weatherResponse = new StringBuilder();
    String line;
    while ((line = brWeather.readLine()) != null) {
        weatherResponse.append(line);
    }
    brWeather.close();

    // Parsear el JSON
    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(weatherResponse.toString());

    // Acceder a las variables dentro de "features" -> "properties" -> "days" -> "variables"
    JsonNode features = root.path("features");
    if (!features.isArray() || features.isEmpty()) {
        throw new Exception("No se encontraron datos en la respuesta del JSON");
    }

    JsonNode variables = features.get(0)
                                 .path("properties")
                                 .path("days")
                                 .get(0)
                                 .path("variables");

    // Variables que vamos a extraer
    String estadoCielo = "";
    String temperatura = "";
    String viento = "";
    String humedad = "";
    String coberturaNubosa = "";

    // Iterar sobre las variables para extraer los datos
    for (JsonNode variable : variables) {
        String name = variable.path("name").asText();

        if (name.equals("sky_state")) {
            estadoCielo = variable.path("values").get(0).path("value").asText(); // Primer estado del cielo
        } else if (name.equals("temperature")) {
            temperatura = variable.path("values").get(0).path("value").asText(); // Primera temperatura
        } else if (name.equals("wind")) {
            viento = variable.path("values").get(0).path("moduleValue").asText(); // Velocidad del viento
        } else if (name.equals("relative_humidity")) {
            humedad = variable.path("values").get(0).path("value").asText(); // Primera humedad
        } else if (name.equals("cloud_area_fraction")) {
            coberturaNubosa = variable.path("values").get(0).path("value").asText(); // Primera cobertura nubosa
        }
    }

    // Crear y devolver el objeto Tiempo
    return new Tiempo(estadoCielo, temperatura, viento, humedad, coberturaNubosa);
}
        

        
 }
    
