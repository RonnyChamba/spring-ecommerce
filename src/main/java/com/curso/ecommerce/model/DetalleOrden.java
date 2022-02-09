package com.curso.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleOrden {

	private Integer id;
	private String nombre;
	private double cantidad;
	private double precio;
	private double total;
}
