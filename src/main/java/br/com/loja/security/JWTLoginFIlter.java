package br.com.loja.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.loja.model.Usuario;

public class JWTLoginFIlter extends AbstractAuthenticationProcessingFilter {

	//Configurando o gerenciado de autenticacao 
	public JWTLoginFIlter(String url, AuthenticationManager authenticationManager) {
		//obriga a autenticar a url
		super(new AntPathRequestMatcher(url));
		
		//gerenciador de autenticação.
		setAuthenticationManager(authenticationManager);
	}
	
	

	
	// Retorna o usuário ao processar a autenticação. 
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		//obtem o usuario.
		Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
		
		// retorna o user com login e senha
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

		try {
			new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		if(failed instanceof BadCredentialsException) {
			response.getWriter().write("User e senha não encontrados");
		}else{
			response.getWriter().write("falha ao logar: " + failed.getMessage());

		}
		
		
		//super.unsuccessfulAuthentication(request, response, failed);
	}

}
