package com.curso.ecommerce.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.IProductoService;

@Controller
@RequestMapping({"/"})
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private IProductoService productoService;
	
	@GetMapping({""})
	public String home(Model model) {
		
		
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}

	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		
		LOGGER.info("Id producto a ver {}", id);
		Producto producto = productoService.get(id).orElse(null);
		model.addAttribute("producto", producto);
		
		return "usuario/productohome";
	}
	
}
