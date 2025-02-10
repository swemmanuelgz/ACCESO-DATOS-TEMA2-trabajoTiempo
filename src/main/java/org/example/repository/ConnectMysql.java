package org.example.repository;

import org.example.Main;
import org.example.model.Tiempo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectMysql {
    //clase para conexcion jdbc
    private static final String URL = "jdbc:mysql://localhost:3307/tiempojson";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public ConnectMysql(){

    }
    public Connection conectar() {
        Connection connection = null;
        try {
            connection = java.sql.DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(Main.ANSI_GREEN +"Conexión establecida"+Main.ANSI_RESET);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    public void saveTiempo(ArrayList<Tiempo> tiempos) {

    }
    //metodo para crear la base de datos en caso de que no exista
    public void createDatabase() {
        //query para crear la base de datos
        String query = "CREATE DATABASE IF NOT EXISTS tiempojson";
        try {
            Connection connection = conectar();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            System.out.println(Main.ANSI_GREEN+"Base de datos creada"+Main.ANSI_RESET);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //query para crear la tabla en la base de datos tiempo con un id autoincremental
        query = "CREATE TABLE IF NOT EXISTS tiempo ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "localidad VARCHAR(255), "
                + "estado_cielo VARCHAR(255), "
                + "temperatura VARCHAR(255), "
                + "viento VARCHAR(255), "
                + "humedad VARCHAR(255), "
                + "cobertura_nubosa VARCHAR(255))";
    }
    //metodo para insertar en la base de datos
    public void insertTiempo(Tiempo tiempo) {
        //query para insertar en la base de datos
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
            System.out.println(Main.ANSI_GREEN+"Tiempo insertado en la base de datos: "+tiempo.getLocalidad()+Main.ANSI_RESET);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //metodo que coge json como parametro y lo inserta en la columna de meteojson en formato json


}
