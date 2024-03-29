package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;

public interface IOrdenService {
	
	Orden save(Orden orden);
	
	List<Orden> findAll();
	
	String generaNumeroOrden();
	
	List<Orden> findByUsuario(Usuario usuario);
	
	Optional<Orden> findById(Integer id);
}
