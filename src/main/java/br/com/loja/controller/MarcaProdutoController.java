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
import br.com.loja.model.MarcaProduto;
import br.com.loja.repository.MarcaRepository;

@RestController
public class MarcaProdutoController {

	@Autowired
	private MarcaRepository marcaRepository;

	@ResponseBody
	@PostMapping(value = "**/salvarMarca")
	public ResponseEntity<MarcaProduto> salvarMarca(@RequestBody @Valid MarcaProduto marca)
			throws ExceptionLojaVirtual {// Recebe o json e converte para objeto

		if (marca.getId() == null) {
			List<MarcaProduto> marcas = marcaRepository.buscarMarcaDesc(marca.getNomeDesc().toUpperCase());

			if (!marcas.isEmpty()) {
				throw new ExceptionLojaVirtual("Já existe uma Marca com a descrição: " + marca.getNomeDesc());
			}
		}

		MarcaProduto marcaSalvo = marcaRepository.save(marca);

		return new ResponseEntity<MarcaProduto>(marcaSalvo, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping(value = "**/deleteMarca")
	public ResponseEntity<?> deleteMarca(@RequestBody MarcaProduto marcas) {
		marcaRepository.deleteById(marcas.getId());

		return new ResponseEntity("Marca Produto Removida", HttpStatus.OK);

	}

	@ResponseBody
	@DeleteMapping(value = "**/deleteMarcaPorId/{id}")
	public ResponseEntity<?> deleteMarcaPorId(@PathVariable("id") Long id) {

		marcaRepository.deleteById(id);

		return new ResponseEntity("Marca Produto Removida", HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/obterMarcaProduto/{id}")
	public ResponseEntity<MarcaProduto> obterMarcaProduto(@PathVariable("id") Long id) throws ExceptionLojaVirtual {

		MarcaProduto marcas = marcaRepository.findById(id).orElse(null);

		if (marcas == null) {
			throw new ExceptionLojaVirtual("Não encontrou Marca com código: " + id);
		}

		return new ResponseEntity<MarcaProduto>(marcas, HttpStatus.OK);

	}

	@ResponseBody
	@GetMapping(value = "**/buscarMarcaProdutoPorDesc/{desc}")
	public ResponseEntity<List<MarcaProduto>> buscarMarcaProdutoPorDesc(@PathVariable("desc") String desc) {

		List<MarcaProduto> marcas = marcaRepository.buscarMarcaDesc(desc.toUpperCase().trim());

		return new ResponseEntity<List<MarcaProduto>>(marcas, HttpStatus.OK);

	}

}
