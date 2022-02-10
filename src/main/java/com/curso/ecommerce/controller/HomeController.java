package com.curso.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.service.IProductoService;

@Controller
@RequestMapping({"/"})
public class HomeController {

	@Autowired
	private IProductoService productoService;
	
	@GetMapping({""})
	public String home(Model model) {
		
		
		model.addAttribute("productos", productoService.findAll());
		return "usuario/home";
	}
	
}
