package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;
import com.curso.ecommerce.model.Usuario;

public interface IUsurioService {
	
	List<Usuario> findAll();
	Usuario save(Usuario usuario);
	Optional<Usuario> get(Integer id);
	void update(Usuario usuario);
	void delete(Integer id); 

}
