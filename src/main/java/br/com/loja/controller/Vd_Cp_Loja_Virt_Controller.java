package br.com.loja.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.ExceptionLojaVirtual;
import br.com.loja.model.Endereco;
import br.com.loja.model.PessoaFisica;
import br.com.loja.model.VendaCompraLojaVirtual;
import br.com.loja.repository.EnderecoRepository;
import br.com.loja.repository.PessoaFisicaRepository;
import br.com.loja.repository.Vd_Cp_loja_virtual_repository;

@RestController
public class Vd_Cp_Loja_Virt_Controller {

	@Autowired
	private Vd_Cp_loja_virtual_repository vd_Cp_loja_virtual_repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	
	@Autowired
	private PessoaController pessoaController;
	
	@ResponseBody
	@PostMapping(value = "**/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaVirtual> salvarVendaLoja(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaVirtual {
		
		
		
		PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);
		
		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);
		
		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);
		
		VendaCompraLojaVirtual salvarVendaLojaSalvo = vd_Cp_loja_virtual_repository.save(vendaCompraLojaVirtual);
		
		return new ResponseEntity<VendaCompraLojaVirtual>(salvarVendaLojaSalvo, HttpStatus.CREATED);
		
		
	}
	
		
	
}
