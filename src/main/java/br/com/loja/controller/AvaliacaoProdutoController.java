package br.com.loja.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import br.com.loja.dto.AvaliacaoProdutoDTO;
import br.com.loja.dto.ImagemProdutoDTO;
import br.com.loja.model.Acesso;
import br.com.loja.model.AvaliacaoProduto;
import br.com.loja.repository.AvaliacaoProdutoRepository;

@RestController
public class AvaliacaoProdutoController {

	@Autowired
	private AvaliacaoProdutoRepository avaliacaoProdutoRepository;
	
	@ResponseBody
	@PostMapping(value = "**/salvarAvalicaoProduto")
	public ResponseEntity<AvaliacaoProduto> salvarAvalicaoProduto(@RequestBody @Valid AvaliacaoProduto avaliacaoProduto) throws ExceptionLojaVirtual {
		
		if (avaliacaoProduto.getEmpresa() == null || (avaliacaoProduto.getEmpresa() != null && avaliacaoProduto.getEmpresa().getId() <= 0)) {
			throw new ExceptionLojaVirtual("Infrome a empresa da avaliação.");
		}
		
		if (avaliacaoProduto.getProduto() == null || (avaliacaoProduto.getProduto() != null && avaliacaoProduto.getProduto().getId() <= 0)) {
			throw new ExceptionLojaVirtual("Informe o produto associado a avaliacao.");

		}
		
		if (avaliacaoProduto.getPessoa() == null || (avaliacaoProduto.getPessoa() != null && avaliacaoProduto.getPessoa().getId() <= 0)) {
			throw new ExceptionLojaVirtual("Informe a pessoa ou cliente associada a avaliacao.");

		}
		
		avaliacaoProduto = avaliacaoProdutoRepository.saveAndFlush(avaliacaoProduto);
		
		return new ResponseEntity<AvaliacaoProduto>(avaliacaoProduto, HttpStatus.CREATED);
		
	}
	
	
	@ResponseBody
	@DeleteMapping(value ="**/deleteAvaliacaoProdutoPorId/{idAvaliacao}")
	public ResponseEntity<?> deleteAvaliacaoProdutoPorId(@PathVariable("idAvaliacao") Long idAvaliacao){
		
		
		avaliacaoProdutoRepository.deleteById(idAvaliacao);
		
		return new ResponseEntity<String>("Avaliacao Removida",HttpStatus.OK);
		
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/AvaliacaoProduto/{idProduto}")
	public ResponseEntity<List<AvaliacaoProdutoDTO>> getAvaliacaoProduto(@PathVariable("idProduto") Long idProduto) {

	    // Buscar todas as avaliações do produto
	    List<AvaliacaoProduto> avaliacoes = avaliacaoProdutoRepository.avaliacaoProduto(idProduto);

	    // Criar lista de DTOs
	    List<AvaliacaoProdutoDTO> avaliacoesDTO = new ArrayList();

	    // Mapear cada AvaliacaoProduto para AvaliacaoProdutoDTO
	    for (AvaliacaoProduto a : avaliacoes) {
	        AvaliacaoProdutoDTO dto = new AvaliacaoProdutoDTO();
	        dto.setId(a.getId());
	        dto.setEmpresa(a.getEmpresa().getId());
	        dto.setProduto(a.getProduto().getId());
	        dto.setNota(a.getNota());
	        dto.setDescricao(a.getDescricao());
	        dto.setPessoa(a.getPessoa().getId());
	        avaliacoesDTO.add(dto);
	    }

	    // Retornar lista de DTOs
	    return new ResponseEntity<>(avaliacoesDTO, HttpStatus.OK);
	}


	
	
	@ResponseBody
	@GetMapping(value ="**/AvalicaoProdutoPessoa/{idProduto}/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProdutoDTO>> AvalicaoProdutoPessoa(
	        @PathVariable("idProduto") Long idProduto, 
	        @PathVariable("idPessoa") Long idPessoa) {

	    List<AvaliacaoProduto> avaliacoes = avaliacaoProdutoRepository.avaliacaoProdutoPessoa(idProduto, idPessoa);

	    List<AvaliacaoProdutoDTO> avaliacoesDTO = new ArrayList<>();

	    for (AvaliacaoProduto a : avaliacoes) {
	        AvaliacaoProdutoDTO dto = new AvaliacaoProdutoDTO();
	        dto.setId(a.getId());
	        dto.setEmpresa(a.getEmpresa().getId());
	        dto.setProduto(a.getProduto().getId());
	        dto.setNota(a.getNota());
	        dto.setDescricao(a.getDescricao());
	        dto.setPessoa(a.getPessoa().getId());
	        avaliacoesDTO.add(dto);
	    }

	    return new ResponseEntity<>(avaliacoesDTO, HttpStatus.OK);
	}

	
	@ResponseBody
	@GetMapping(value ="**/AvaliacaoPessoa/{idPessoa}")
	public ResponseEntity<List<AvaliacaoProdutoDTO>> AvaliacaoPessoa(@PathVariable("idPessoa") Long idPessoa) {

	    List<AvaliacaoProduto> avaliacoes = avaliacaoProdutoRepository.avaliacaoPessoa(idPessoa);

	    List<AvaliacaoProdutoDTO> avaliacoesDTO = new ArrayList<>();

	    for (AvaliacaoProduto a : avaliacoes) {
	        AvaliacaoProdutoDTO dto = new AvaliacaoProdutoDTO();
	        dto.setId(a.getId());
	        dto.setEmpresa(a.getEmpresa().getId());
	        dto.setProduto(a.getProduto().getId());
	        dto.setNota(a.getNota());
	        dto.setDescricao(a.getDescricao());
	        dto.setPessoa(a.getPessoa().getId());
	        avaliacoesDTO.add(dto);
	    }

	    return new ResponseEntity<>(avaliacoesDTO, HttpStatus.OK);
	}

	
	
	
}
