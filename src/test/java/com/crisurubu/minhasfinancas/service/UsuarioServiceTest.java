package com.crisurubu.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.crisurubu.minhasfinancas.model.entities.Usuario;
import com.crisurubu.minhasfinancas.model.exceptions.ErroAutenticacao;
import com.crisurubu.minhasfinancas.model.exceptions.RegraNegocioException;
import com.crisurubu.minhasfinancas.model.repository.UsuarioRepository;
import com.crisurubu.minhasfinancas.model.service.impl.UsuarioServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean
	UsuarioRepository repository;
	
	
	
	@Test(expected = Test.None.class)
	public void deveSalvarUsuario() {
		//cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l).nome("usuario").email("cris@gmail.com").senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificacao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("usuario");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("cris@gmail.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test
	public void deveAutenticarUsuarioComSucesso() {
		//cenario
		String email = "cris@gmail.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email) ).thenReturn(Optional.of(usuario));
		
		//acao
		Usuario result = service.autenticar(email, senha);
		
		//verificacao
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQuandoNaoEncontraUsuarioCadastradoComEmailInformado() {
		//cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
	
		//acao
		Throwable exception =  Assertions.catchThrowable(() -> service.autenticar("cris@gmail.com", "senha"));
		
		//verificacao
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessageContainingAll("Usuario não encontrado");
		
		
	}
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("cris@gamil.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//acao
		Throwable exception =  Assertions.catchThrowable(() -> service.autenticar("cris@gmail.com", "123"));
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessageContainingAll("Senha inválida");
	}
	
	
	
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		
 
			// cenario
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
			
 
			// acao
			service.validarEmail("cris@gmail.com");
		
	}
 
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
	
			//cenario
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
			
 
			//acao
			service.validarEmail("cris@gmail.com");
	
	}
}