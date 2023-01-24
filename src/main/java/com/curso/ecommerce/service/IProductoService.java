package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Producto;

public interface IProductoService {

	
	List<Producto> findAll();
	Producto save(Producto producto);
	Optional<Producto> get(Integer id);
	void update(Producto producto);
	void delete(Integer id); 
	
}
