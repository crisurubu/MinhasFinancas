package com.crisurubu.minhasfinancas.model.service;

import com.crisurubu.minhasfinancas.model.entities.Usuario;

public interface UsuarioService {
	
	Usuario autenticar(String email, String senha);
	Usuario salvarUsuario(Usuario usuario);
	
	void validarEmail(String email);

}
