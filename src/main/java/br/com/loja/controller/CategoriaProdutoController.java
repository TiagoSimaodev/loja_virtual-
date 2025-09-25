package br.com.loja.controller;

import java.util.List;

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
import br.com.loja.dto.CategoriaProdutoDTO;
import br.com.loja.model.Acesso;
import br.com.loja.model.CategoriaProduto;
import br.com.loja.repository.CategoriaProdutoRepository;

@RestController
public class CategoriaProdutoController {
	
	
	@Autowired
	private CategoriaProdutoRepository categoriaProdutoRepository;
	
	
	
	@ResponseBody
	@GetMapping(value ="**/buscarPorDescCategoria/{desc}")
	public ResponseEntity<List<CategoriaProduto>> buscarPorDescCategoria(@PathVariable("desc") String desc){
		
		
		List<CategoriaProduto> categoriaDesc = categoriaProdutoRepository.buscarCategoriDesc(desc.toUpperCase());
		
		return new ResponseEntity<List<CategoriaProduto>>(categoriaDesc,HttpStatus.OK);
		
		
	}
	
	
	
	@ResponseBody
	@DeleteMapping(value ="**/deleteCategoria")
	public ResponseEntity<?> deleteCategoria(@RequestBody CategoriaProduto categoriaProduto){
		
		if(categoriaProdutoRepository.findById(categoriaProduto.getId()).isPresent()  == false) {
			return new ResponseEntity("Categoria ja foi Removida",HttpStatus.OK);

		}
		
		categoriaProdutoRepository.deleteById(categoriaProduto.getId());
		
		return new ResponseEntity("Categoria Removida",HttpStatus.OK);
		
		
	}
	
	
	@ResponseBody
	@PostMapping(value = "**/salvarCategoria")
	public ResponseEntity<CategoriaProdutoDTO> salvarCategoria(@RequestBody CategoriaProduto categoriaProduto) throws ExceptionLojaVirtual {
		
		if(categoriaProduto.getEmpresa() == null || (categoriaProduto.getEmpresa().getId() == null)) {
			throw new ExceptionLojaVirtual("A empresa deve ser informada.");
		}
		
		if(categoriaProduto.getId() == null && categoriaProdutoRepository.existeCategoria(categoriaProduto.getNomeDesc())) {
			throw new ExceptionLojaVirtual("Não pode cadastrar categoria com o mesmo nome. ");
		}
		
		
		
		CategoriaProduto categoriaSalva = categoriaProdutoRepository.save(categoriaProduto);
		
		CategoriaProdutoDTO categoriaProdutoDTO = new CategoriaProdutoDTO();
		categoriaProdutoDTO.setId(categoriaSalva.getId());
		categoriaProdutoDTO.setNomeDesc(categoriaSalva.getNomeDesc());
		categoriaProdutoDTO.setEmpresa(categoriaSalva.getEmpresa().getId().toString());
		
		return new ResponseEntity<CategoriaProdutoDTO>(categoriaProdutoDTO, HttpStatus.OK);
		
	}
	
}
