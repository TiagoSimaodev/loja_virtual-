package br.com.loja.security;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
		
		
		// usado para ver no postman, para teste. 
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
	}
	
	
	
}
