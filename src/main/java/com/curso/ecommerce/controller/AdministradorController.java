package com.curso.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.IProductoService;
import com.curso.ecommerce.service.IUsurioService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUsurioService usurioService;
	
	@GetMapping({"", "/"})
	public String home(Model model) {
		
		List<Producto> productos = productoService.findAll();
		
		model.addAttribute("productos", productos);
		return "administrador/home";
	}
	
	@GetMapping("/usuarios")
	public String usuarios(Model model) {

		model.addAttribute("usuarios", usurioService.findAll());
		
		return "administrador/usuarios";
	}
	

}
