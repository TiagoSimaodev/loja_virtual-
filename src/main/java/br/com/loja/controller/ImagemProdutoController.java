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

import br.com.loja.model.ImagemProduto;
import br.com.loja.repository.ImagemProdutoRepository;

@RestController
public class ImagemProdutoController {

	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarImagemProduto")
	public ResponseEntity<ImagemProduto> salvarImagemProduto(@RequestBody @Valid ImagemProduto imagemProduto) {
		
		ImagemProduto imagemProdutoSalvo = imagemProdutoRepository.saveAndFlush(imagemProduto);
		
		return new ResponseEntity<ImagemProduto>(imagemProdutoSalvo, HttpStatus.CREATED);
		
	}
	
	
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteTodasImagemProduto/{idProduto}")
	public ResponseEntity<?> deleteTodasImagemProduto(@PathVariable("idProduto") Long idProduto) {
		
		imagemProdutoRepository.deleteImagens(idProduto);
		
		return new ResponseEntity<String>("Imagegens do produto removida", HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteImagemObjeto")
	public ResponseEntity<?> deleteImagemObjeto(@RequestBody ImagemProduto imagemProduto) {
		
		imagemProdutoRepository.deleteById(imagemProduto.getId());
		
		return new ResponseEntity<String>("Imagem removido", HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteImagemProdutoPorId/{id}")
	public ResponseEntity<?> deleteImagemProdutoPorId(@PathVariable("id") Long id) {
		
		imagemProdutoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Imagem removido", HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/obterImagemPorProduto/{idProduto}")
	public ResponseEntity<List<ImagemProduto>> obterImagemPorProduto(@PathVariable("idProduto") Long idProduto) {
		List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(idProduto);
		
		
		return new ResponseEntity<List<ImagemProduto>>(imagemProdutos, HttpStatus.OK);
		
		
	}
	
	
}
