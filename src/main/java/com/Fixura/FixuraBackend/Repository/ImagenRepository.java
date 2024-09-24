package com.Fixura.FixuraBackend.Repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.Fixura.FixuraBackend.Model.Imagen;
import com.Fixura.FixuraBackend.Repository.Interface.IimagenRepository;

@Repository
public class ImagenRepository implements IimagenRepository{
    private static final String GITHUB_API_URL = "https://api.github.com/repos/KevinGM02/Galeria-Imagenes-Fixura/contents/imagenes";
    @Value("${github.secret}")
    private String GITHUB_TOKEN;
    //private static final String REPO_OWNER = "KevinGM02";
    //private static final String REPO_NAME = "Galeria-Imagenes-Fixura";
    private static final String BRANCH = "main";
    
    @Override
	public int save(Imagen imagen) {
		 // Preparar la URL para la solicitud PUT
        String apiUrl = GITHUB_API_URL + "/" + imagen.getFileName();

        // Crear el JSON payload
        String jsonPayload = "{\n" +
                "  \"message\": \"Subir imagen " + imagen.getFileName() + "\",\n" +
                "  \"content\": \"" + imagen.getEncodedImage() + "\",\n" +
                "  \"branch\": \"" + BRANCH + "\"\n" +
                "}";

        // Headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + GITHUB_TOKEN);
        headers.add("Content-Type", "application/json");

        // Preparar la solicitud PUT
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);
        RestTemplate restTemplate = new RestTemplate();
        
        // Ejecutar la solicitud PUT
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return 1;
        } else {
            return 0;
        }
	}
}
