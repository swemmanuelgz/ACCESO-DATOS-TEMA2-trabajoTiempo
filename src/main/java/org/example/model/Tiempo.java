package org.example.model;

//Clase Tiempo con atributos y getters y setters y metodos y toString
public class Tiempo {
    private String localidad;
    private  String estadoCielo;
    private String temperatura;
    private String viento;
    private String humedad;
    private String coberturaNubosa;
    public Tiempo() {
    }
    public Tiempo(String localidad,String estadoCielo, String temperatura, String viento, String humedad, String coberturaNubosa) {
        this.estadoCielo = estadoCielo;
        this.temperatura = temperatura;
        this.viento = viento;
        this.humedad = humedad;
        this.coberturaNubosa = coberturaNubosa;
        this.localidad = localidad;
    }
    public String getEstadoCielo() {
        return estadoCielo;
    }
    public void setEstadoCielo(String estadoCielo) {
        this.estadoCielo = estadoCielo;
    }
    public String getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }
    public String getViento() {
        return viento;
    }
    public void setViento(String viento) {
        this.viento = viento;
    }
    public String getLocalidad() {
        return localidad;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }
    
    public String getHumedad() {
        return humedad;
    }
    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }
    public String getCoberturaNubosa() {
        return coberturaNubosa;
    }
    public void setCoberturaNubosa(String coberturaNubosa) {
        this.coberturaNubosa = coberturaNubosa;
    }
    @Override
    public String toString() {
        return "Tiempo [estadoCielo=" + estadoCielo + "\n temperatura=" + temperatura + "\n viento=" + viento
                + "\n humedad=" + humedad + "\n coberturaNubosa=" + coberturaNubosa + "]";
    }
    

    
}

