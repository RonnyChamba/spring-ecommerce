package com.curso.ecommerce.service;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Usuario;

@Service
public class UserDetailServiceImpl  implements UserDetailsService{

	
	@Autowired
	private IUsurioService usurioService;
	
	//@Autowired
	// Para encryptar y descryptar la contraseña
	//@Lazy para resolver la dependencias circulares
	//private  @Lazy BCryptPasswordEncoder bCrypt;
	
	@Autowired
	HttpSession session;
	
	private static final Logger LOGGER = LoggerFactory.getLogger( UserDetailServiceImpl.class);
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		LOGGER.info("Usuario username a logear: {}", username );
		
		Optional<Usuario> optionalUsuario = usurioService.findByEmail(username);
			
		if (optionalUsuario.isPresent()){
			
			Usuario usuario = optionalUsuario.get();
			
			LOGGER.info("Id del usario: {}", usuario.getId());
			
			session.setAttribute("idusuario", usuario.getId());
			
			return User.builder()
								.username(usuario.getNombre())
								.password(usuario.getPassword())
								.roles(usuario.getTipo())
								.build();	
		}
		
		
		throw new  UsernameNotFoundException("Usuario no encontrado");
	}

}
