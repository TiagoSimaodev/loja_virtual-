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
import br.com.loja.model.Produto;
import br.com.loja.repository.ProdutoRepository;

@RestController
public class ProdutoController {

	
	
	
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	
	@ResponseBody // Poder dar um retorno da API
	@PostMapping(value = "**/salvarProduto") // mapeando a url para receber JSON
	public ResponseEntity<Produto> salvarProduto(@RequestBody @Valid Produto produto) throws ExceptionLojaVirtual {//Recebe o json e converte para objeto
		
		//para validar se existe empresa, para evitar nullpointerException
		if(produto.getEmpresa() == null || produto.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaVirtual("Empresa responsável deve ser informada");

		}
		
		
		if (produto.getId() == null) {
			List<Produto> produtos = produtoRepository.buscarProdutoNome(produto.getNome().toUpperCase(), produto.getEmpresa().getId());
			
			if(!produtos.isEmpty()) {
				throw new ExceptionLojaVirtual("Já existe um Produto com esse nome: " + produto.getNome());
			}
		}
		
		
		
		if(produto.getCategoriaProduto() == null || produto.getCategoriaProduto().getId() <= 0) {
			throw new ExceptionLojaVirtual("A Categoria deve ser informada. ");
		}
		
		if (produto.getMarcaProduto() == null || produto.getMarcaProduto().getId() <= 0) {
			throw new ExceptionLojaVirtual("A Marca deve ser informada");
		}
		
		Produto produtoSalvo = produtoRepository.save(produto);
		
		return new ResponseEntity<Produto>(produtoSalvo, HttpStatus.OK);
	}
	
	@ResponseBody
	@PostMapping(value ="**/deleteProduto")
	public ResponseEntity<String> deleteProduto(@RequestBody Produto produto){
		produtoRepository.deleteById(produto.getId());
		
		return new ResponseEntity<String>("Produto Removido",HttpStatus.OK);
		
		
	}
	
	@ResponseBody
	@DeleteMapping(value ="**/deleteProdutoPorId/{id}")
	public ResponseEntity<String> deleteProdutoPorId(@PathVariable("id") Long id){
		
		
		produtoRepository.deleteById(id);
		
		return new ResponseEntity<String>("Produto Removido",HttpStatus.OK);
		
		
	}
	
	@ResponseBody
	@GetMapping(value ="**/obterProduto/{id}")
	public ResponseEntity<Produto> obterProduto(@PathVariable("id") Long id) throws ExceptionLojaVirtual{
		
		
		Produto produto =produtoRepository.findById(id).orElse(null);
		
		if(produto == null) {
			throw new ExceptionLojaVirtual("Não encontrou produto  com código: " + id);
		}
		
		
		return new ResponseEntity<Produto>(produto,HttpStatus.OK);
		
		
	}
	
	@ResponseBody
	@GetMapping(value ="**/buscarProdutoNome/{nome}")
	public ResponseEntity<List<Produto>> buscarProdutoNome(@PathVariable("nome") String nome){
		
		
		List<Produto> produto = produtoRepository.buscarProdutoNome(nome.toUpperCase());
		
		return new ResponseEntity<List<Produto>>(produto,HttpStatus.OK);
		
		
	}
	
	
	
	
}
