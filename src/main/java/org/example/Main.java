package org.example;

import javax.swing.JOptionPane;

import org.example.controller.TiempoController;
import org.example.model.Tiempo;

public class Main {
    public static void main(String[] args) {
        String location = "Santiago";
        Tiempo tiempo = new TiempoController().getTiempo(location);
        JOptionPane.showMessageDialog(null, tiempo.toString());
    }
}