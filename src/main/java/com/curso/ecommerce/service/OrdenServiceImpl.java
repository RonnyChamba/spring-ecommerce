package com.curso.ecommerce.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {

		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {

		return ordenRepository.findAll();
	}

	/**
	 * Metodo encargado de generar el nuevo numero de orden
	 * @return : El nuevo numero de orden convertido a String
	 */
	@Override
	public String generaNumeroOrden() {

		Integer numero = 0;
		String numeroConcatenado = "";

		List<Orden> ordenes = findAll();

		List<Integer> numeros = ordenes.stream().map(od -> Integer.parseInt(od.getNumero()))
				.collect(Collectors.toList());

		numero = ordenes.isEmpty() ? 1 : (numeros.stream().max((num1, num2) -> Integer.compare(num1, num2)).get() + 1);

		numeroConcatenado = getNuevoNumeroOrden(numero);
		
		return numeroConcatenado;
	}

	/**
	 * 
	 * @param numOrden : Numero para generar el nuevo numero de orden
	 * @return : Un String con el patron nuevo para el numero de orden
	 */
	private String getNuevoNumeroOrden(Integer numOrden) {

		String newNumOrden = "";

		// Modelo numero; 0000000001
		if (numOrden < 10) {
			newNumOrden = "000000000".concat(String.valueOf(numOrden));
			
		} else if (numOrden < 100) {
			newNumOrden = "00000000".concat(String.valueOf(numOrden));
		
		} else if (numOrden < 1000) {
			newNumOrden = "0000000".concat(String.valueOf(numOrden));
		
		} else if (numOrden < 10000) {
			newNumOrden = "000000".concat(String.valueOf(numOrden));
		
		} else {
			newNumOrden = "00000".concat(String.valueOf(numOrden));
		}

		return newNumOrden;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		
		return ordenRepository.findById(id);
	}

}
