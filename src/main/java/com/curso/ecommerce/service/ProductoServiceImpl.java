package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.repository.ProductoRepositoy;

@Service
public class ProductoServiceImpl implements IProductoService {

	
	@Autowired
	private ProductoRepositoy productoRepositoy;

	@Override
	public List<Producto> findAll() {
		
		return productoRepositoy.findAll();
	}

	@Override
	public Producto save(Producto producto) {
	
		return productoRepositoy.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		
		return productoRepositoy.findById(id);
	}

	@Override
	public void update(Producto producto) {
		
		productoRepositoy.save(producto);

	}

	@Override
	public void delete(Integer id) {
		
		productoRepositoy.deleteById(id);

	}

}
