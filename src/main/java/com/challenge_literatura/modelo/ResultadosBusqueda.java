package com.challenge_literatura.modelo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultadosBusqueda(@JsonAlias("count") Integer cuenta,
                                 @JsonAlias("results") List<DatosLibro> resultado) {


}
