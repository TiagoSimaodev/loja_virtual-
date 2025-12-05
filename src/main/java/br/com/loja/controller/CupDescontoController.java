package br.com.loja.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.ExceptionLojaVirtual;
import br.com.loja.model.CupDesc;
import br.com.loja.repository.CupDescontoRepository;

@RestController
public class CupDescontoController {

	@Autowired
	private CupDescontoRepository cupDescontoRepository;
	
	
	
	@ResponseBody
	@GetMapping(value = "**/obterCumpoDescPor/{id}")
	public ResponseEntity<CupDesc> obterCumpoDescPorId(@PathVariable("id") Long id) throws ExceptionLojaVirtual {
		
		CupDesc cupDesc = cupDescontoRepository.findById(id).orElse(null);
		
		if(cupDesc == null) {
			throw new ExceptionLojaVirtual("Não encontrou Cupom desconto com o codigo" + id);
			
		}
		
		return new ResponseEntity<CupDesc>(cupDesc, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteCupomPorId/{id}")
	public ResponseEntity<?> deleteCupomPorId(@PathVariable("id") Long id) {
		
		 cupDescontoRepository.deleteById(id);
		
		return new ResponseEntity("Cupom Produto Removio", HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@PostMapping(value ="**/salvarCupDesc")
	public ResponseEntity<CupDesc> salvarCupDesc(@RequestBody @Valid CupDesc cupDesc){
		
		CupDesc cupDesc2 = cupDescontoRepository.save(cupDesc);
		
		return new ResponseEntity<CupDesc>(cupDesc2, HttpStatus.CREATED);
		
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/listaCupDesco/{idEmpresa}")
	public ResponseEntity<List<CupDesc>> listaCupDesco(@PathVariable("idEmpresa") Long idEmpresa) {
		
		return new ResponseEntity<List<CupDesc>>(cupDescontoRepository.cupDescontoPorEmpresa(idEmpresa), HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/listaCupDesco")
	public ResponseEntity<List<CupDesc>> listaCupDesco() {
		
		return new ResponseEntity<List<CupDesc>>(cupDescontoRepository.findAll(), HttpStatus.OK);
		
	}
	
}
