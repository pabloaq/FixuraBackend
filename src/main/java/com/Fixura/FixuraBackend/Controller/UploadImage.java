package com.Fixura.FixuraBackend.Controller;

import java.io.IOException;
import java.util.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.Fixura.FixuraBackend.Model.Imagen;
import com.Fixura.FixuraBackend.Repository.Interface.IimagenRepository;

@RestController
@RequestMapping("/api/v1/upload")
@CrossOrigin(origins = "http://localhost:4200")
public class UploadImage {

    @Autowired
	private IimagenRepository iimagenRepository;

    @PostMapping(value="/image" ,consumes=MediaType.MULTIPART_FORM_DATA_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Por favor selecciona una imagen.");
        }
        Imagen imagen = new Imagen();
        imagen.setEncodedImage(Base64.getEncoder().encodeToString(IOUtils.toByteArray(file.getInputStream())));
        imagen.setFileName(file.getOriginalFilename());
        int result= iimagenRepository.save(imagen);
        if(result == 1) {
            return ResponseEntity.ok("{\"message\": \"Imagen subida exitosamente a GitHub.\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Error al subir la imagen a GitHub.\"}");
        }
    }

}
