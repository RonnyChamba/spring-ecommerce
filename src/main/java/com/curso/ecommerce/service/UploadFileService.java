package com.curso.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public final class UploadFileService {

	
	private final String FOLDER = "images//";
	
	
	public String saveImage(MultipartFile file) throws IOException {
		
		
		if (file.isEmpty()) {
			
			// Obtener los bytes de la imagen
			byte [] bytes = file.getBytes();
			
			// Path donde se guardara la imagen
			Path path = Paths.get(FOLDER + file.getOriginalFilename());
			
			Files.write(path, bytes);
			
			return file.getOriginalFilename();
		}
		
		return "default.jpg";
	}
	
	public void deleteImage(String nombre) {
		
		// Ruta base donde se encuentra la imagen
		String ruta = FOLDER;
		
		File file = new File(ruta + nombre);
		
		file.delete();
	}
	 
}
