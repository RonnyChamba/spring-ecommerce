package com.curso.ecommerce.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IProductoService;
import com.curso.ecommerce.service.IUsurioService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AdministradorController.class);
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUsurioService usurioService;
	
	@Autowired
	private IOrdenService ordenService;
	
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
	
	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		
		model.addAttribute("ordenes", ordenService.findAll());	
		
		return "administrador/ordenes";
	}
	
	@GetMapping("/detalle/{id}")
	 public String  detalleCompra( @PathVariable Integer id, Model model) {
		 
		 LOGGER.info("Id de orden : ",id);
		 
		 Orden orden = ordenService.findById(id).orElse(null);
		 
		 List<DetalleOrden> detalles = orden.getDetalles();
		 
		 model.addAttribute("detalles", detalles);
		 return "administrador/detalleorden";
	 }

}
