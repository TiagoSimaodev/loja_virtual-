package br.com.loja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.model.Acesso;
import br.com.loja.repository.AcessoRepository;
import br.com.loja.service.AcessoService;

@Controller
@RestController
public class AcessoController {

	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private AcessoRepository acessoRepository;
	
	
	@ResponseBody // Poder dar um retorno da API
	@PostMapping(value = "**/salvarAcesso") // mapeando a url para receber JSON
	public ResponseEntity<Acesso> salvarAcesso(@RequestBody Acesso acesso) {//Recebe o json e converte para objeto
		
		Acesso acessoSalvo = acessoService.save(acesso);
		
		return new ResponseEntity<Acesso>(acessoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value ="**/deleteAcesso")
	public ResponseEntity<?> deleteAcesso(@RequestBody Acesso acesso){
		acessoRepository.deleteById(acesso.getId());
		
		return new ResponseEntity(HttpStatus.OK);
		
		
	}
	
	
	
	
}
