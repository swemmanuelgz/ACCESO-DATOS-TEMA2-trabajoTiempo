package org.example;

import org.example.repository.ConnectMysql;
import org.example.repository.ConnectSQLite;
import org.example.repository.TiempoRepository;
import org.example.view.PantallaPrincipal;

public class Main {
    //Codigos de colores ANSI
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static void main(String[] args) throws Exception {

        TiempoRepository tiempoRepository = new TiempoRepository();
        ConnectMysql conectar = new ConnectMysql();
        ConnectSQLite mysqLite = new ConnectSQLite();
        mysqLite.createDatabase();
        conectar.createDatabase();

        tiempoRepository.updateAllTiempoSql();
        PantallaPrincipal pantalla = new PantallaPrincipal();
        pantalla.setVisible(true);
        pantalla.setLocationRelativeTo(null);
    }
}