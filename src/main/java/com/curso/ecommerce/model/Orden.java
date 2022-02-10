package com.curso.ecommerce.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ORDENES")
//@ToString(exclude = {"detalle"})
public class Orden {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String numero;
	private LocalDate fechaCreacion;
	private LocalDate fechaRecibida;
	private double total;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Usuario usuario;
	
	@OneToOne(mappedBy = "orden")
	private DetalleOrden detalle;
	

}
