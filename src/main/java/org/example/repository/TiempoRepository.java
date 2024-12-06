package org.example.repository;

import java.net.MalformedURLException;
import java.net.URL;

import org.example.model.Tiempo;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TiempoRepository {

    //Api 
    private  static final String API_URL = "https://www.meteogalicia.gal/web/rss-georss-json";
    private static final String API_KEY ="MWXPF8V59rh5BX8G983Bj4aWCm08aalDegD98fIKni4C2lK5jJnMIhA11lbde1MF";

        //Metodo para conseguir el Tiempo
        public Tiempo getTiempo(String location) throws MalformedURLException{

            //construccion de la url
            String urlString = API_URL + "?location=" + location + "&API_KEY=" + API_KEY;
            URL url = new URL(urlString);

            ObjectMapper mapper = new ObjectMapper();

            return new Tiempo();
        }

        
 }
    
