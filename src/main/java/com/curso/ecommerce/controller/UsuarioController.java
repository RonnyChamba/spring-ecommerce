package com.curso.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@GetMapping("/registro")
	public String create() {
		
		return "usuario/registro";
	}
	
	@PostMapping("/save")
	public String save(Usuario usuario) {
		
		LOGGER.info("Usuario a registrar: {}", usuario);
		usuario.setTipo("USER");
		
		usurioService.save(usuario);
		
		return "redirect:/";
	}
	

	@GetMapping("/login")
	public String login() {
		
		return "usuario/login";
	}
	
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session, RedirectAttributes redirectAttributes) {
	
		LOGGER.info("Accesos {}", usuario);
		
		Optional<Usuario> user = usurioService.findByEmail(usuario.getEmail());
		
		if (user.isPresent()) {
			
			session.setAttribute("idusuario", user.get().getId());
			redirectAttributes.addFlashAttribute("msgLogin", "Bievenido al sistema");
			return user.get().getTipo().equals("ADMIN") ?
					                                     "redirect:/administrador"
													    :"redirect:/";		
		}	
		LOGGER.info("Usuario no existe");
		redirectAttributes.addFlashAttribute("msgLogin", "Usuario  <strong> %s </strong>  no esta registrado".formatted(usuario.getEmail()));
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
	
}
