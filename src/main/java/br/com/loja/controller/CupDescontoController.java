package br.com.loja.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.model.CupDesc;
import br.com.loja.repository.CupDescontoRepository;

@RestController
public class CupDescontoController {

	@Autowired
	private CupDescontoRepository cupDescontoRepository;
	
	
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
