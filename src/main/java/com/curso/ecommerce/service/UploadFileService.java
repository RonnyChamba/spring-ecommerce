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

	
	//private final String FOLDER = "src//main//resources//static/img";
	private final String FOLDER = "images//";

	
	
	public String saveImage(MultipartFile file) throws IOException {
		
		// Si selecciona una imagen
		if (!file.isEmpty()) {
			
			// Directorio de img 
			Path directorioImg = Paths.get(FOLDER);
			
			String rutaAbsoluta = directorioImg.toFile().getAbsolutePath();
			
			// Obtener los bytes de la imagen
			byte [] bytes = file.getBytes();
			
			
			// Path donde se guardara la imagen
			Path pathCompleto = Paths.get(rutaAbsoluta+"//"+file.getOriginalFilename());
			
			Files.write(pathCompleto, bytes);
			
			return file.getOriginalFilename();
		}
		
		// Imagen por default, si no selecciona una imagen
		return "default.jpg";
	}
	
	public void deleteImage(String nombre) {
		
		// Ruta base donde se encuentra la imagen
		Path directorioImg = Paths.get(FOLDER);
		
		String rutaAbsoluta = directorioImg.toFile().getAbsolutePath();
		File file = new File(rutaAbsoluta +"//"+nombre);
		
		file.delete();
	}
	 
}
