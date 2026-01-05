package com.aluracursos.screenmatch.model;

public enum Categoria {
    ACCION("Action", "Accion"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    DRAMA("Drama", "Drama"),
    CRIMEN("Crime", "Crimen"),
    AVENTURA("Adventure", "Aventura"),
    ;

    //ATRIBUTOS
    private String categoriaOmdb;
    private String categoriaEnEspanol;

    //CONSTRUCTOR
    Categoria(String categoriaOmdb, String categoriaEnEspanol) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEnEspanol = categoriaEnEspanol;
    }

    //METODOS
    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            // Compara el texto que llega con el atributo categoriaOmdb ("Action", "Drama", etc.)
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoría encontrada: " + text);
    }

    public static Categoria fromEspanol(String text){
       for (Categoria categoria : Categoria.values()){
           if (categoria.categoriaEnEspanol.equalsIgnoreCase(text)){
               return categoria;
           }
       }
        throw new IllegalArgumentException("Ninguna categoría encontrada: " + text);
    }
}
