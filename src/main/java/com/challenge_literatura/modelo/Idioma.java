package com.challenge_literatura.modelo;

public enum Idioma {
    ESPANOL("es", "Spanish"),
    INGLES("en", "English"),
    FRANCES("fr", "French"),
    ITALIANO("it", "Italian"),
    PORTUGUES("pt", "Portuguese"),
    ALEMAN("de", "German"),
    RUSO("ru", "Russian"),
    CHINO("zh", "Chinese"),
    JAPONES("ja", "Japanese");
    private String languagesGudetex;
    private String idiomaEspanol;

    private Idioma(String languagesGudetex, String idiomaEspanol) {
        this.languagesGudetex = languagesGudetex;
        this.idiomaEspanol = idiomaEspanol;
    }

    public static Idioma fromString(String idioma) {
        for (Idioma i : Idioma.values()) {
            if (i.languagesGudetex.equalsIgnoreCase(idioma)) {
                return i;
            }
        }
        return null;
    }

    public static Idioma fromEspanol(String idioma) {
        for (Idioma i : Idioma.values()) {
            if (i.idiomaEspanol.equalsIgnoreCase(idioma)) {
                return i;
            }
        }
        return null;
    }

    public String getLanguagesGudetex() {
        return languagesGudetex;
    }

    public String getIdiomaEspanol() {
        return idiomaEspanol;
    }


}
