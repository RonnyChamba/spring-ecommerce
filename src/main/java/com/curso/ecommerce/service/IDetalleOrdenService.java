package com.curso.ecommerce.service;

import org.springframework.stereotype.Repository;

import com.curso.ecommerce.model.DetalleOrden;

@Repository
public interface IDetalleOrdenService {

	DetalleOrden save(DetalleOrden detalleOrden);
	
	
}
