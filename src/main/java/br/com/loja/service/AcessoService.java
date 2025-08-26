package br.com.loja.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.loja.model.Acesso;
import br.com.loja.repository.AcessoRepository;

@Service
public class AcessoService {

	@Autowired
	private AcessoRepository acessoRepository;
	
	public Acesso save (Acesso acesso) {
		// qualquer tipo de validação.
		
		return acessoRepository.save(acesso);
	}
	
}
