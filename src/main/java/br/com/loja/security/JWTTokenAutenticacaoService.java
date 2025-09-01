package br.com.loja.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.loja.ApplicationContextLoad;
import br.com.loja.model.Usuario;
import br.com.loja.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//Criar a autenticacao e retornar também a autenticacao JWT
@Service
@Component
public class JWTTokenAutenticacaoService {
	
	//token de validade de 11 dias
	private static final long EXPIRATION_TIME = 959990000;
	
	//chave de senha para juntar com o jwt
	private static final String SECRET = "senhasecreta";
	
	private static final String TOKEN_PREFIX = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	
	// gera o token e da a resposta para o cliente com o jWT
	public void addAuthentication(HttpServletResponse response, String username) throws Exception {
		
		// montagem do token
		
		String JWT = Jwts.builder(). // chama o gerador de token
				setSubject(username) // adiciona o user
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))// tempo de expiração
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		
		// EX: Bearer skke242j423hh2o2o2j,2k331o1.3o1j31j31o1j313o1
		String token = TOKEN_PREFIX + " " + JWT;
		
		// Dá a resposta para a tela e para o cliente. EX: outra api, navegador, aplicativo, etc.. 
		response.addHeader(HEADER_STRING, token);
		
		
		liberacaoCors(response);
		
		
		// usado para ver no postman, para teste. 
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
	
	// Retorna o usuario validado com token ou caso não seja valido retorna null
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		//pega o token para validar.
		String token = request.getHeader(HEADER_STRING);
		
		if(token != null) { 
			String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();
			
			//faz a validação do token do usuario na requisição e obter o user.
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(tokenLimpo)
					.getBody().getSubject(); // pegar o usuario. ex: admin ou alex
			
			if(user != null) {
				Usuario usuario = ApplicationContextLoad
						.getApplicationContext()
						.getBean(UsuarioRepository.class).findUserByLogin(user);
				
				
				if(usuario != null) {
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getSenha(), 
							usuario.getAuthorities());
				}
				
			}
			
		}
		
		
		liberacaoCors(response);
		return null;
	}
	
	
	
	// fazendo liberação contra erro de cors no navegador;
	private void liberacaoCors(HttpServletResponse response)  {
		
		if(response.getHeader("Access-Control-Allow-Origin") == null ) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Headers") == null ) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		
		
		if(response.getHeader("Access-Control-Request-Headers") == null ) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
		
		if(response.getHeader("Access-Control-Allow-Methods") == null ) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}
		
		
	}
	
	
	
	
	
}
