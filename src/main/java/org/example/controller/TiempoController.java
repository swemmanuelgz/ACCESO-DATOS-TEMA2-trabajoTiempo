package org.example.controller;

import javax.swing.JOptionPane;

import org.example.Main;
import org.example.model.Tiempo;
import org.example.repository.TiempoRepository;

public class TiempoController {
    // repositorio
    private final TiempoRepository tiempoRepository ;
    //Constructor
    public TiempoController() {
        this.tiempoRepository = new TiempoRepository();
    }
    //Consegumos el tiempo a partir de la lozalización que introducimos
    public Tiempo getTiempo(String location){
        try {
            return this.tiempoRepository.getTiempo(location);
        } catch (Exception e) {
            //Excepcion en caso de error
            JOptionPane.showMessageDialog(null, "Error al conseguir el tiempo");
           System.out.println(Main.ANSI_RED+e.getMessage()+Main.ANSI_RESET);

        }
        return null;
    }

}
