package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.repository.UsuarioRepository;

import net.bytebuddy.asm.Advice.Return;

@Service
public class UsuarioServiceImpl  implements IUsurioService {

	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Override
	public List<Usuario> findAll() {
		
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario save(Usuario usuario) {
		
		return usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> get(Integer id) {
		
		return usuarioRepository.findById(id);
	}

	@Override
	public void update(Usuario usuario) {
	
		usuarioRepository.save(usuario);
		
	}

	@Override
	public void delete(Integer id) {

		usuarioRepository.deleteById(id);
		
	}

	@Override
	public Optional<Usuario> findByEmail(String email) {
		
		return  usuarioRepository.findByEmail(email);
	}

}
