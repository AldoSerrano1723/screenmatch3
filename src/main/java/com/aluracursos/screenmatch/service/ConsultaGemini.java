package com.aluracursos.screenmatch.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class ConsultaGemini {
    public static String obtenerTraduccion(String texto){
        String apiKey = "AIzaSyBxwcNj82yKnqvHZCHrejq31MTatUxpalw";
        String modelo = "gemini-2.5-flash";
        String prompt = "Traduce el siguiente texto al español. solo pon la traduccion: " + texto;
        Client cliente = Client.builder().apiKey(apiKey).build();

        try {
            GenerateContentResponse respuesta = cliente.models.generateContent(
                    modelo,
                    prompt,
                    null // Parámetro para configuraciones adicionales
            );

            if (!respuesta.text().isEmpty()) {
                return respuesta.text();
            }
        } catch (Exception e) {
            System.err.println("Error al llamar a la API de Gemini para traducción: " + e.getMessage());
        }

        return null;
    }
}
