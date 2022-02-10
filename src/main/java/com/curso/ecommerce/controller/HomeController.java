package com.curso.ecommerce.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.IProductoService;

@Controller
@RequestMapping({"/"})
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private IProductoService productoService;
	
	private List<DetalleOrden> detalles = new ArrayList<>();
	
	private Orden orden = new Orden();;
	
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
	
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam  Integer cantidad, Model model) {
		
		Producto producto = productoService.get(id).orElse(null);
		LOGGER.info("Producto añadido al carrito {}", producto);
		if (producto !=null) {
			
			
			
			DetalleOrden detalleOrden =  new DetalleOrden();
			double total =0;
			
			detalleOrden.setNombre(producto.getNombre());			
			detalleOrden.setPrecio(producto.getPrecio());
			detalleOrden.setCantidad(cantidad);
			detalleOrden.setProducto(producto);
			detalleOrden.setTotal(detalleOrden.getCantidad() * detalleOrden.getPrecio());
			detalles.add(detalleOrden);
			
			total = detalles
							.stream()
							.mapToDouble(dt -> dt.getTotal())
							.sum();
			
			orden.setTotal(total);
			
			
			model.addAttribute("cards", detalles);
			model.addAttribute("orden", orden);
		}
		
		return "usuario/carrito";
	}
	
	@GetMapping("/delete/card/{id}")
	public String deleteProductoCard(@PathVariable Integer id, Model model) {
		
		double nuevoTotal = 0;
		List<DetalleOrden> nuevoDetalle = detalles
												 .stream()
												 .filter(dto ->  dto.getProducto().getId() !=id )
												 .collect(Collectors.toList());
		
		// Actualizar lista 
		detalles = nuevoDetalle;
		
		nuevoTotal = detalles
				.stream()
				.mapToDouble(dt -> dt.getTotal())
				.sum();
		
		orden.setTotal(nuevoTotal);
		model.addAttribute("cards", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
}
