package br.com.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServiceContagemAcessoApi {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void atualizaAcessoEndPointPf() {
	
		jdbcTemplate.execute("begin; update tabela_acesso_end_point set qtd_acesso_end_point = qtd_acesso_end_point + 1 where nome_and_point = 'END-POINT-NOME-PESSOA-FISICA' ; commit;");
	
	}
	
	
}
