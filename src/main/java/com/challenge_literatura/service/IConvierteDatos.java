package com.challenge_literatura.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String datos, Class<T> clase);
}
