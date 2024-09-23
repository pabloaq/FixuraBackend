package com.Fixura.FixuraBackend.Service;
import com.Fixura.FixuraBackend.Model.Imagen;
import com.Fixura.FixuraBackend.Repository.ImagenRepository;
import com.Fixura.FixuraBackend.Service.Interface.IimagenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagenService implements IimagenService{

    @Autowired
	private ImagenRepository imagenRepository;

    @Override
	public int save(Imagen imagen) {
		int exitosamente;
		try {
			exitosamente=imagenRepository.save(imagen);
		}catch (Exception ex) {
			throw ex;
		}
		
		return exitosamente;
	}
}
