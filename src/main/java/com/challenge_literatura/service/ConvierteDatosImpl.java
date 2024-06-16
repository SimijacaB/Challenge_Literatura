package com.challenge_literatura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatosImpl implements IConvierteDatos{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String datos, Class<T> clase) {

        try {
            //Convierte los datos en un objeto de la clase que se le pase
            return objectMapper.readValue(datos, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
