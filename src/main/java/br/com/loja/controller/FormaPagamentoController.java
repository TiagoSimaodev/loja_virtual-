package br.com.loja.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.ExceptionLojaVirtual;
import br.com.loja.model.Acesso;
import br.com.loja.model.FormaPagamento;
import br.com.loja.repository.FormaPagamentoRepository;

@RestController
public class FormaPagamentoController {

	@Autowired
	private FormaPagamentoRepository formaPagamentoRepository;
	
	@ResponseBody
	@PostMapping(value = "**/SalvarFormaPagamento") 
	public ResponseEntity<FormaPagamento> SalvarFormaPagamento(@RequestBody @Valid FormaPagamento formaPagamento) throws ExceptionLojaVirtual {//Recebe o json e converte para objeto
		
		FormaPagamento formaPagamentoSalvo = formaPagamentoRepository.save(formaPagamento);
		
		return new ResponseEntity<FormaPagamento>(formaPagamentoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping(value = "**/listaFormaPagamento")
	public ResponseEntity<List<FormaPagamento>> listaFormaPagamento(){
		
		return new ResponseEntity<List<FormaPagamento>>(formaPagamentoRepository.findAll(), HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/listaFormaPagamentoEmpresa/{idEmpresa}")
	public ResponseEntity<List<FormaPagamento>> listaFormaPagamentoEmpresa(@PathVariable(value = "idEmpresa") Long idEmpresa){
		
		return new ResponseEntity<List<FormaPagamento>>(formaPagamentoRepository.findAll(idEmpresa), HttpStatus.OK);
		
	}
	
	
	
}
