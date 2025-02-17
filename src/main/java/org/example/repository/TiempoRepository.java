package org.example.repository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.example.Main;
import org.example.model.Tiempo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TiempoRepository {

    // Api
    private static final String FIND_PLACE_URL = "https://servizos.meteogalicia.gal/apiv4/findPlaces?location=";
    private static final String WEATHER_URL = "https://servizos.meteogalicia.gal/apiv4/getNumericForecastInfo";
    private static final String API_KEY = "MWXPF8V59rh5BX8G983Bj4aWCm08aalDegD98fIKni4C2lK5jJnMIhA11lbde1MF";
    private ConnectMysql connect = new ConnectMysql();

    // metodo conseguir tiempo sql
    public Tiempo getTiempoSql(String tiempoName) throws Exception {
        return connect.getTiempo(tiempoName);
    }

    // Metodo para conseguir el Tiempo
    public Tiempo getTiempo(String location) throws Exception {
        // Endpoint para buscar el ID de la localidad
        String findPlaceURL = FIND_PLACE_URL + location + "&API_KEY=" + API_KEY;
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
        // System.out.println(Main.ANSI_BLUE+"Respuesta de búsqueda de localidad:
        // "+findPlaceResponse+Main.ANSI_RESET);
        String line;
        while ((line = brFindPlace.readLine()) != null) {
            findPlaceResponse.append(line);
        }
        brFindPlace.close();

        // Parsear JSON de búsqueda de localidad
        ObjectMapper mapper = new ObjectMapper();
        JsonNode findPlaceRoot = mapper.readTree(findPlaceResponse.toString());

        // System.out.println(Main.ANSI_BLUE+"JSON de búsqueda de localidad:
        // "+findPlaceRoot+Main.ANSI_RESET);
        JsonNode places = findPlaceRoot.path("features");

        if (places.isEmpty() || !places.isArray()) {
            throw new Exception("No se encontró la localidad: " + location);

        }

        // cOGEMOS LA COORDENADAS
        JsonNode primerLugar = places.get(0);
        JsonNode coordenadas = primerLugar.path("geometry").path("coordinates");

        String locationID = places.get(0).path("properties").path("id").asText();

        if (!coordenadas.isArray() || coordenadas.size() != 2) {
            throw new Exception("No se encontró la localidad: " + location);
        }
        double lon = coordenadas.get(0).asDouble();
        double lat = coordenadas.get(1).asDouble();
        System.out.println(Main.ANSI_BLUE + "Coordenadas de la localidad: [" + lon + "," + lat + " ]ID de la localidad:"
                + locationID + Main.ANSI_RESET);
        // Endpoint para obtener el tiempo usando el ID de la localidad
        String weatherURL = WEATHER_URL + "?coords=" + lon + "," + lat
                + "&variables=temperature,wind,sky_state,relative_humidity,cloud_area_fraction&API_KEY=" + API_KEY;
        URL weatherEndpoint = new URL(weatherURL);
        System.out.println(Main.ANSI_BLUE + "URL del tiempo: " + weatherURL + Main.ANSI_RESET);

        HttpURLConnection weatherConnection = (HttpURLConnection) weatherEndpoint.openConnection();
        try {
            weatherConnection.setRequestMethod("GET");
            weatherConnection.setRequestProperty("x-api-key", API_KEY);
        } catch (ProtocolException e) {
            System.out.println(Main.ANSI_RED + e.getMessage() + Main.ANSI_RESET);
        }

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
        // System.out.println("JSON del tiempo: " + weatherResponse.toString());

        // Parsear el JSON del tiempo
        JsonNode weatherRoot = mapper.readTree(weatherResponse.toString());

        // System.out.println(Main.ANSI_BLUE+"JSON del tiempo:
        // "+weatherRoot+Main.ANSI_RESET);
        JsonNode variables = weatherRoot.path("features").path(0).path("properties").path("days").path(0)
                .path("variables");
        // System.out.println(Main.ANSI_BLUE+"\nVariables: "+variables+Main.ANSI_RESET);

        // Ajustar las claves según la respuesta real de la API

        String estadoCielo = getFirstValue(variables, "sky_state");
        String temperatura = getFirstValue(variables, "temperature");
        String viento = getFirstValue(variables, "wind");
        String humedad = getFirstValue(variables, "relative_humidity");
        String coberturaNubosa = getFirstValue(variables, "cloud_area_fraction");

        // Crear y devolver el objeto Tiempo
        return new Tiempo(location, estadoCielo, temperatura, viento, humedad, coberturaNubosa);
    }

    public String getFirstValue(JsonNode jsonNode, String nombreVariable) {
        for (JsonNode variable : jsonNode) { // Iterar sobre las variables
            if (variable.path("name").asText().equals(nombreVariable)) {
                JsonNode values = variable.path("values");
                if (values.isArray() && values.size() > 0) {
                    // Tomar el primer valor disponible
                    String valor = values.get(0).path("value").asText("No disponible");
                    if (nombreVariable.equals("wind")) {
                        valor = values.get(0).path("moduleValue").asText("No disponible");
                    }
                    System.out.println(Main.ANSI_BLUE + "Valor de la variable '" + nombreVariable + "': " + valor
                            + Main.ANSI_RESET);

                    return valor;
                }
            }
        }
        // Si no encuentra la variable, retorna un error
        System.out.println(
                Main.ANSI_RED + "Variable '" + nombreVariable + "' no encontrada o sin valores." + Main.ANSI_RESET);
        return "No disponible";
    }

    public void updateAllTiempoSql() throws Exception {
        String[] ciudades = { "coru", "Lugo", "Ourense", "Pontevedra", "Vigo", "Santiago", "Ferrol" };
        ArrayList<Tiempo> tiemposDatos = new ArrayList<>();
        // Borramos los datos de sql
        connect.deleteDatosTiempo();

        // bucle para coger los datos de las ciudades
        for (String ciudade : ciudades) {
            Tiempo tiempo = getTiempo(ciudade);
            tiemposDatos.add(tiempo);
        }

        System.out.println(Main.ANSI_BLUE + "Tiempos añadidos al arraylist" + Main.ANSI_RESET);

        // Buble para meter los tiempos en la base de datos
        for (int i = 0; i < tiemposDatos.size(); i++) {
            // hacemos un insert con cada tiempo
            connect.insertTiempo(tiemposDatos.get(i));
        }

    }

}
