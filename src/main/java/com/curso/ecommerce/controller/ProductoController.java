 package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IProductoService;
import com.curso.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
    private UploadFileService upload;
	
	@GetMapping({"/", ""})
	public String show(Model model) {
		
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String  save(Producto producto, @RequestParam("img") MultipartFile file ) throws IOException {
		
		
		Usuario usuario = new Usuario(1, "", "", "", "", "", "", "", null, null);
		producto.setUsuario(usuario);
		
		String nombreImagen = upload.saveImage(file);
		producto.setImagen(nombreImagen);
		
		LOGGER.info("Este es el objeto de la vista {}", producto);
		
		productoService.save(producto);
		
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, RedirectAttributes redirectAttributes, Model model) {
		
		Producto producto =  null;
		
		Optional<Producto> optionalProducto = productoService.get(id);
		
		producto = optionalProducto.orElse(null);
		
		LOGGER.info("Producto buscado {}",producto);
		
		if (producto ==null) {
			redirectAttributes.addFlashAttribute("msg", "El producto con ID %s no existe".formatted(id));
			return "redirect:/productos";
		}
		
		model.addAttribute("producto", producto);
		return "productos/edit";
	}
	
	
	@PostMapping("/update")
	public String update(Producto producto,  @RequestParam("img") MultipartFile file) throws IOException {
		
	
		LOGGER.info("Producto a actualizar {}", producto);
		
		// Consulto el estado actual del producto
		Producto p = productoService.get(producto.getId()).orElse(null);
		
		if (file.isEmpty()) { // Si editamos el producto pero no cambios de imagen
			producto.setImagen(p.getImagen());
			
		}else {// Si Editamos el producto y cambios la imagen f
				
			if (!p.getImagen().equalsIgnoreCase("default.jpg")
					&& !p.getImagen().equalsIgnoreCase("default.png")) {
				
				// Eliminar la img anterior
				upload.deleteImage(p.getImagen());
			}
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
			
		}
		
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		
		Producto producto =  productoService.get(id).orElse(null);
		
		LOGGER.info("Producto buscado a eliminar {}",producto);
		
		if (producto ==null) {
			redirectAttributes.addFlashAttribute("msg", "El producto con ID %s no existe".formatted(id));
		}else {
			
		
			// Cuando elimine el producto, tambien se eliminara la img del servidor,
			// exceto si el la img por default
			if (!producto.getImagen().equalsIgnoreCase("default.jpg") 
					&& !producto.getImagen().equalsIgnoreCase("default.png")) {
				
				upload.deleteImage(producto.getImagen());
			}
			productoService.delete(id);
		}
		
		return "redirect:/productos";
	}
	
	
}
