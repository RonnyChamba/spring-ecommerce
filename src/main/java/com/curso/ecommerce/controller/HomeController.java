package com.curso.ecommerce.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

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
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IProductoService;
import com.curso.ecommerce.service.IUsurioService;

@Controller
@RequestMapping({"/"})
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IUsurioService usurioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private IDetalleOrdenService detalleOrdenService;
	
	
	private List<DetalleOrden> detalles = new ArrayList<>();
	
	private Orden orden = new Orden();;
	
	@GetMapping({""})
	public String home(Model model, HttpSession session) {
		
		LOGGER.info("Sesion de usurio :{}", session.getAttribute("idusuario"));
		
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
	public String addCart(@RequestParam Integer id, @RequestParam  Integer cantidad, 
			              Model model) {
		
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
			
			// Validr que no se añade el mismo producto mas de una vez a la misma orden
			Integer idProducto = producto.getId();
			boolean isExiste = detalles.stream().anyMatch(dtp -> dtp.getProducto().getId() ==idProducto); 
			
			if (!isExiste) detalles.add(detalleOrden);
			
			model.addAttribute("msg",  !isExiste? "El producto <strong > %s </strong> fue agregado correctamente ".formatted(producto.getNombre())
											     : "El producto <strong > %s </strong> ya esta registrado".formatted(producto.getNombre()));
			model.addAttribute("tipoMsg", !isExiste);
			
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
		
		
		model.addAttribute("msg", "Producto eliminado correctamente");
		model.addAttribute("tipoMsg", true);
		
		return "usuario/carrito";
	}
	
	@GetMapping("/getCard")
	public String getCard(Model model) {
		
		model.addAttribute("cards", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	 @GetMapping("/order")
	 public String order(Model model, HttpSession session) {
		 
		 
		 Usuario usuario = usurioService.get(Integer.parseInt(session.getAttribute("idusuario").toString()))
				 						.orElse(null);
		 
		model.addAttribute("cards", detalles);
		model.addAttribute("orden", orden); 
		model.addAttribute("usuario", usuario);
		return "usuario/resumenorden";
	 }
	
	 @GetMapping("/saveOrder")
	public String saveOrder(HttpSession session){
		
		 LocalDate date = LocalDate.now();
		 
		 orden.setFechaCreacion(date);
		 orden.setNumero( ordenService.generaNumeroOrden());
		 
		 Usuario usuario = usurioService.get(Integer.parseInt(session.getAttribute("idusuario").toString()))
				 						.orElse(null);
		 
		 orden.setUsuario(usuario);
		 
		 // Aqui a cad detalle de la lista se asigna la orden, ver metodo
		 orden.setDetalles(detalles);
		 
		 // Al persistir la orden tambien se persistirá los detalles de la orden
		 ordenService.save(orden);
		 
		 LOGGER.info("Orden guardadad: {}", orden);
		 	 
		 // Limpiar lista y orden
		 orden = new Orden();
		 detalles.clear();
		 
		 return "redirect:/";
	}
	 
	 /**
	  * 
	  * @param nombre: Nombre del producto a buscar
	  * @param model : Model de datos enviados a la vista
	  * @return : Lista de productos encontrados, si el nombre a buscar viene vacio, listará todos los productos
	  */
	 @PostMapping("/search")
	 public String searchProducto(@RequestParam String nombre, Model model) {
		 
		  LOGGER.info("Nombre del producto a buscar: {} ", nombre);
		 
		  List<Producto> productos = productoService.findAll();
		
		  if (nombre !=null && !nombre.isBlank()) {
			  
			  List<Producto> productosFiltro = productos
						 .stream()
						 .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
						 .collect(Collectors.toList());
			  
			  if (productosFiltro.size()<1) {
				  model.addAttribute("msgSearch","No se encontraron  coincidencias para el nombre <strong> %s </strong>".formatted(nombre));
			  }
			  
			  LOGGER.info("Longitud productos encontrados: {}", productosFiltro.size() );
			  
			  
			  productos = productosFiltro;
			  
		  } 
		  
			 model.addAttribute("productos",productos);
		 return "usuario/home";
	 }
	 
}
