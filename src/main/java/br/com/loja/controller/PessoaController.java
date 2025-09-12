package br.com.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.ExceptionLojaVirtual;
import br.com.loja.model.PessoaJuridica;
import br.com.loja.repository.PessoaRepository;
import br.com.loja.service.PessoaUserService;

@RestController
public class PessoaController {

	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	//endpoint é microservicoe é um api
	@ResponseBody
	@PostMapping(value = "**/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtual {
		
		if (pessoaJuridica == null) {
			throw new ExceptionLojaVirtual("Pessoa juridica não pode ser Null");
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null ) {
			throw new ExceptionLojaVirtual("Já existe um CNPJ cadastrado com o numero: " + pessoaJuridica.getCnpj());
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeInscEstadualCadastrado(pessoaJuridica.getInscrEstadual()) != null ) {
			throw new ExceptionLojaVirtual("Já existe Inscrição estadual cadastradoa com o numero: " + pessoaJuridica.getInscrEstadual());
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
}
