package com.curso.ecommerce.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orden {
	
	
	private Integer id;
	private String numero;
	private LocalDate fechaCreacion;
	private LocalDate fechaRecibida;
	private double total;
	

}
