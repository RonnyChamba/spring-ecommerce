package com.curso.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringBootSecurity extends  WebSecurityConfigurerAdapter {
	
	
	
	// Interface propia de spring, aqui se inyecta un bean de la clase UserDetailServiceImpl
	// ya que dicha clase, implementa esta interface
	@Autowired
	private UserDetailsService userDetailService;
	
	/**
	 * Metodo que nos permite que el usuario sea el correcto
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
		// Ejecutara el metodo loadUserByUsername implementado en UserDetailServiceImpl
		// que se encarga de validar el usuario
		auth.userDetailsService(userDetailService).passwordEncoder(getEncoder());
	}
	
	
	@Bean
	public BCryptPasswordEncoder getEncoder() {		
		return new BCryptPasswordEncoder();
	}
	
	
	/**
	 * Metodo que nos permite restringuir el acceso a  ciertas recursos a un usuario
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/**
		 * csrf() : metodo que permite evitar que se inyecte codigo malisioso a la aplicacion
		 * 
		 */
		
		http.csrf().disable().authorizeRequests()
												// Especificar las vistas a que vista tiene acceso un rol especifico
												.antMatchers("/administrador/**").hasRole("ADMIN")
												.antMatchers("/productos/**").hasRole("ADMIN")
												// Especificr Pagina de login
												.and().formLogin().loginPage("/usuario/login")
												// Para los demas roles, permitir todo, ademas indicar indicar
												// url que redireccionar cuando se logee correctamente
												.permitAll().defaultSuccessUrl("/usuario/acceder");
	}

}
