package br.com.loja.security;

import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.loja.service.ImplementacaoUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter implements HttpSessionListener {

    
	@Autowired
	private ImplementacaoUserDetailsService  implementacaoUserDetailsService;
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//ativar o nivel de autenticação por token
		http.csrf().csrfTokenRepository(
				CookieCsrfTokenRepository.
				withHttpOnlyFalse())
				.disable().authorizeRequests().antMatchers("/").permitAll()
				.antMatchers("/index").permitAll()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
		
			// redireciona ou da um retorno para o index quando deslogar.
				.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
				
				//mapeia o logout do sistema
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				
				//Filtra as requisições para login de JWT
				.and().addFilterAfter(new JWTLoginFIlter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
				
		
	}
	
	//Irá consultar o user no banco com o Spring Security
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
		
	}
	
	
	// Ignora algumas url livre de autenticação
	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.ignoring().antMatchers(HttpMethod.GET, "/salvarAcesso", "/deleteAcesso")
		//.antMatchers(HttpMethod.POST, "/salvarAcesso", "/deleteAcesso"); 
		//ignorando url no momento para não autenticar. 
	}
	
}
