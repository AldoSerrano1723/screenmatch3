package com.aluracursos.screenmatch.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConsultaGemini {
    //METODOS
    public static String obtenerTraduccion(String texto){

        String apiKey = System.getenv("GEMINI_API_KEY");
        String modelo = "gemini-2.5-flash";
        String prompt = "Traduce el siguiente texto al español. solo pon la traduccion: " + texto;

        //Validación simple para evitar errores si la variable no existe
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error: La variable de entorno GEMINI_API_KEY no está configurada.");
            return null;
        }

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
