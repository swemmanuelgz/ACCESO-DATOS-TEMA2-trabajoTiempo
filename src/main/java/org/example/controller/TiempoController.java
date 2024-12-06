package org.example.controller;

import javax.swing.JOptionPane;

import org.example.model.Tiempo;
import org.example.repository.TiempoRepository;

public class TiempoController {
    private final TiempoRepository tiempoRepository ;

    public TiempoController() {
        this.tiempoRepository = new TiempoRepository();
    }
    //Consegumos el tiempo a partir de la lozalización que introducimos
    public Tiempo getTiempo(String location){
        try {
            return this.tiempoRepository.getTiempo(location);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conseguir el tiempo");
            e.printStackTrace();

        }
        return null;
    }

}
