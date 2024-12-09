package org.example.generators;


import java.io.FileWriter;
import java.io.IOException;
import org.example.model.Tiempo;
import java.util.List;

public class CsvGenerator {
// Método para generar un archivo CSV a partir de una lista de objetos Tiempo
        public void generateCSV(List<Tiempo> tiempos, String filename) throws IOException {
        // Crear el archivo CSV
        FileWriter csvWriter = new FileWriter(filename);

        // Escribir el encabezado del CSV
        csvWriter.append("Estado Cielo,Temperatura,Viento,Humedad,Cobertura Nubosa\n");

        // Escribir los datos de los tiempos en el archivo
        for (Tiempo tiempo : tiempos) {
            csvWriter.append(tiempo.getEstadoCielo()).append(",")
                    .append(tiempo.getTemperatura()).append(",")
                    .append(tiempo.getViento()).append(",")
                    .append(tiempo.getHumedad()).append(",")
                    .append(tiempo.getCoberturaNubosa()).append("\n");
        }

        // Cerrar el escritor de archivos
        csvWriter.flush();
        csvWriter.close();
    }
}