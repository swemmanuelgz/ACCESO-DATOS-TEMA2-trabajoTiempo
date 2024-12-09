package org.example;

import javax.swing.JOptionPane;

import org.example.controller.TiempoController;
import org.example.model.Tiempo;
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
    public static void main(String[] args) {
        String location = "Santiago";
        Tiempo tiempo = new TiempoController().getTiempo(location);
        //Suso haz la inyerfaz donde se pueda introducir la localizacion y muestre su tiempo como resultado
        System.out.println(tiempo.toString());
        JOptionPane.showMessageDialog(null, tiempo.toString());
        PantallaPrincipal pantalla = new PantallaPrincipal();
        pantalla.setVisible(true);
        pantalla.setLocationRelativeTo(null);
    }
}