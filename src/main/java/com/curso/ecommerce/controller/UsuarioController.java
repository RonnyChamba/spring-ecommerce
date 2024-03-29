package com.curso.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.curso.ecommerce.editors.FirstLetterUpperCase;
import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsurioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);

	
	@Autowired
	private IUsurioService usurioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	BCryptPasswordEncoder passEconde = new BCryptPasswordEncoder();
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		
		binder.registerCustomEditor(String.class, "nombre",  new FirstLetterUpperCase("upper"));
		
	}
	
	@GetMapping("/registro")
	public String create() {
		
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		
		LOGGER.info("Usuario a registrar: {}", usuario);
		usuario.setTipo("USER");
		usuario.setPassword(passEconde.encode(usuario.getPassword()));
		usurioService.save(usuario);
		return "redirect:/";
	}
	

	@GetMapping("/login")
	public String login() {
		
		return "usuario/login";
	}
	
	@GetMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session, RedirectAttributes redirectAttributes) {
	
		LOGGER.info("Accesos {}", usuario);
		
		Optional<Usuario> user = usurioService.get(Integer.parseInt(session.getAttribute("idusuario").toString()));
		
		if (user.isPresent()) {
			
			session.setAttribute("idusuario", user.get().getId());
			redirectAttributes.addFlashAttribute("msgLogin", "Bievenido al sistema");
			return user.get().getTipo().equals("ADMIN") ?
					                                     "redirect:/administrador"
													    :"redirect:/";		
		}	
		LOGGER.info("Usuario no existe");
		redirectAttributes.addFlashAttribute("msgLogin", String.format("Usuario  <strong> %s </strong>  no esta registrado",usuario.getEmail()));
		return  "redirect:/usuario/login";
	}
	
	
	 @GetMapping("/compras")
	 public String obtenerComprass(Model model, HttpSession session) {
		 
		 model.addAttribute("session", session.getAttribute("idusuario"));
		 
		 Usuario usuario = usurioService.get( Integer.parseInt(session.getAttribute("idusuario").toString()))
				 .orElse(null);
		 
		 List<Orden> ordenes = ordenService.findByUsuario(usuario);
		 
		 model.addAttribute("ordenes", ordenes);
		 return "usuario/compras";
	 }
	 
	 @GetMapping("/detalle/{id}")
	 public String  detalleCompra( @PathVariable Integer id, 
			 						HttpSession session, Model model) {
		 
		 LOGGER.info("Id de orden : ",id);
		 
		 Orden orden = ordenService.findById(id).orElse(null);
		 
		 List<DetalleOrden> detalles = orden.getDetalles();
		 
		 model.addAttribute("session",session.getAttribute("idusuario"));		 
		 model.addAttribute("detalles", detalles);
		 return "usuario/detallecompra";
	 }
	 
	 @GetMapping("/cerrar")
	 public String cerrar(HttpSession session) {
		 
		 session.removeAttribute("idusuario");
		 return "redirect:/";
	 }
	
}
