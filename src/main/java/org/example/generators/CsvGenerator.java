package org.example.generators;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.example.model.Tiempo;

public class CsvGenerator {
    // Método para generar un archivo CSV a partir de una lista de objetos Tiempo
    public void generateCSV(List<Tiempo> tiempos, String filename) throws IOException {
        // Crear el archivo CSV
        FileWriter csvWriter = new FileWriter(filename);

        // Escribir el encabezado del CSV, incluyendo "Localidad"
        csvWriter.append("Localidad;Estado Cielo;Temperatura;Viento;Humedad;Cobertura Nubosa\n");

        // Escribir los datos de los tiempos en el archivo
        for (Tiempo tiempo : tiempos) {
            csvWriter.append(escapeCsv(tiempo.getLocalidad())).append(";") // Campo de localidad
                    .append(escapeCsv(tiempo.getEstadoCielo())).append(";")
                    .append(escapeCsv(tiempo.getTemperatura())).append(";")
                    .append(escapeCsv(tiempo.getViento())).append(";")
                    .append(escapeCsv(tiempo.getHumedad())).append(";")
                    .append(escapeCsv(tiempo.getCoberturaNubosa())).append("\n");
        }

        // Cerrar el escritor de archivos
        csvWriter.flush();
        csvWriter.close();
    }

    // Método para escapar valores y asegurar que se escriban correctamente en el CSV
    private String escapeCsv(String value) {
        if (value == null) {
            return ""; // Manejar valores nulos
        }
        if (value.contains(";") || value.contains("\n") || value.contains("\"")) {
            value = value.replace("\"", "\"\""); // Escapar comillas dobles
            return "\"" + value + "\""; // Encapsular en comillas dobles
        }
        return value;
    }
}