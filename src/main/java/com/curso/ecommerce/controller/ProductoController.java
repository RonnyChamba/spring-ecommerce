package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	
	@Autowired
	private IProductoService productoService;
	
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
	public String  save(Producto producto) {
		
		LOGGER.info("Este es el objeto de la vista {}", producto);
		
		Usuario usuario = new Usuario(1, "", "", "", "", "", "", "", null, null);
		producto.setUsuario(usuario);
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
	public String update(Producto producto) {
		
		LOGGER.info("Producto a actualizar {}", producto);
		productoService.update(producto);
		
		return "redirect:/productos";
	}
	
	
}
