package br.com.loja.controller;

import java.util.ArrayList;
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

import br.com.loja.dto.ImagemProdutoDTO;
import br.com.loja.model.ImagemProduto;
import br.com.loja.repository.ImagemProdutoRepository;

@RestController
public class ImagemProdutoController {

	@Autowired
	private ImagemProdutoRepository imagemProdutoRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarImagemProduto")
	public ResponseEntity<ImagemProdutoDTO> salvarImagemProduto(@RequestBody @Valid ImagemProduto imagemProduto) {

		imagemProduto = imagemProdutoRepository.saveAndFlush(imagemProduto);

		ImagemProdutoDTO imagemProdutoSalvoDTO = new ImagemProdutoDTO();
		imagemProdutoSalvoDTO.setId(imagemProduto.getId());
		imagemProdutoSalvoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
		imagemProdutoSalvoDTO.setProduto(imagemProduto.getProduto().getId());
		imagemProdutoSalvoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
		imagemProdutoSalvoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());

		return new ResponseEntity<ImagemProdutoDTO>(imagemProdutoSalvoDTO, HttpStatus.CREATED);

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

		if (imagemProdutoRepository.existsById(imagemProduto.getId())) {
			return new ResponseEntity<String>(
					"Imagem já foi removida ou não existe com esse id: " + imagemProduto.getId(), HttpStatus.OK);

		}

		imagemProdutoRepository.deleteById(imagemProduto.getId());

		return new ResponseEntity<String>("Imagem removido", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteImagemProdutoPorId/{id}")
	public ResponseEntity<?> deleteImagemProdutoPorId(@PathVariable("id") Long id) {

		if (imagemProdutoRepository.existsById(id)) {
			return new ResponseEntity<String>("Imagem já foi removida ou não existe com esse id: " + id, HttpStatus.OK);

		}

		imagemProdutoRepository.deleteById(id);

		return new ResponseEntity<String>("Imagem removido", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/obterImagemPorProduto/{idProduto}")
	public ResponseEntity<List<ImagemProdutoDTO>> obterImagemPorProduto(@PathVariable("idProduto") Long idProduto) {
		List<ImagemProduto> imagemProdutos = imagemProdutoRepository.buscaImagemProduto(idProduto);

		List<ImagemProdutoDTO> dtos = new ArrayList<ImagemProdutoDTO>();

		for (ImagemProduto imagemProduto : imagemProdutos) {

			ImagemProdutoDTO imagemProdutoSalvoDTO = new ImagemProdutoDTO();
			imagemProdutoSalvoDTO.setId(imagemProduto.getId());
			imagemProdutoSalvoDTO.setEmpresa(imagemProduto.getEmpresa().getId());
			imagemProdutoSalvoDTO.setProduto(imagemProduto.getProduto().getId());
			imagemProdutoSalvoDTO.setImagemMiniatura(imagemProduto.getImagemMiniatura());
			imagemProdutoSalvoDTO.setImagemOriginal(imagemProduto.getImagemOriginal());
			dtos.add(imagemProdutoSalvoDTO);	
		}

		return new ResponseEntity<List<ImagemProdutoDTO>>(dtos, HttpStatus.OK);

	}

}
