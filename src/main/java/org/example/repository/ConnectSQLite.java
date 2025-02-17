package org.example.repository;

import org.example.Main;
import org.example.model.Tiempo;

import java.sql.*;
import java.util.ArrayList;

public class ConnectSQLite {
    private static final String URL = "jdbc:sqlite:tiempojson.db";

    public ConnectSQLite() {
        createDatabase();
    }

    public Connection conectar() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println(Main.ANSI_GREEN + "Conexión establecida" + Main.ANSI_RESET);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public void createDatabase() {
        String query = "CREATE TABLE IF NOT EXISTS tiempo ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "localidad TEXT, "
                + "estado_cielo TEXT, "
                + "temperatura TEXT, "
                + "viento TEXT, "
                + "humedad TEXT, "
                + "cobertura_nubosa TEXT)";
        try {
            Connection connection = conectar();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println(Main.ANSI_GREEN + "Base de datos y tabla creadas" + Main.ANSI_RESET);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertTiempo(Tiempo tiempo) {
        String query = "INSERT INTO tiempo (localidad, estado_cielo, temperatura, viento, humedad, cobertura_nubosa) VALUES (?,?,?,?,?,?)";
        try {
            Connection connection = conectar();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, tiempo.getLocalidad());
            preparedStatement.setString(2, tiempo.getEstadoCielo());
            preparedStatement.setString(3, tiempo.getTemperatura());
            preparedStatement.setString(4, tiempo.getViento());
            preparedStatement.setString(5, tiempo.getHumedad());
            preparedStatement.setString(6, tiempo.getCoberturaNubosa());
            preparedStatement.executeUpdate();
            System.out.println(Main.ANSI_GREEN + "Tiempo insertado: " + tiempo.getLocalidad() + Main.ANSI_RESET);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDatosTiempo() {
        String queryDelete = "DELETE FROM tiempo";
        try {
            Connection conexion = conectar();
            PreparedStatement preparedStatement = conexion.prepareStatement(queryDelete);
            preparedStatement.executeUpdate();
            System.out.println(Main.ANSI_GREEN + "Eliminando datos de tiempo" + Main.ANSI_RESET);
            conexion.close();
        } catch (SQLException e) {
            System.out.println(Main.ANSI_RED + e + Main.ANSI_RESET);
        }
    }

    public Tiempo getTiempo(String ciudad) {
        String query = "SELECT * FROM tiempo WHERE localidad = ?";
        Tiempo tiempo = null;
        try {
            Connection connection = conectar();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, ciudad);
            ResultSet resultset = preparedStatement.executeQuery();
            if (resultset.next()) {
                tiempo = new Tiempo(
                        resultset.getString("localidad"),
                        resultset.getString("estado_cielo"),
                        resultset.getString("temperatura"),
                        resultset.getString("viento"),
                        resultset.getString("humedad"),
                        resultset.getString("cobertura_nubosa")
                );
            }
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tiempo;
    }
}

