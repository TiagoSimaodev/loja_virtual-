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

import br.com.loja.ExceptionLojaVirtual;
import br.com.loja.dto.NotaFiscalVendaDTO;
import br.com.loja.dto.ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO;
import br.com.loja.model.NotaFiscalCompra;
import br.com.loja.model.NotaFiscalVenda;
import br.com.loja.repository.NotaFiscalCompraRepository;
import br.com.loja.repository.NotaIFiscalVendaRepository;
import br.com.loja.service.NotaFiscalCompraService;

@RestController
public class NotaFiscalCompraController {

	@Autowired
	private NotaFiscalCompraRepository notaFiscalCompraRepository;
	
	@Autowired
	private NotaIFiscalVendaRepository notaIFiscalVendaRepository;
	
	@Autowired
	private NotaFiscalCompraService notaFiscalCompraService;
	
	@ResponseBody
	@PostMapping(value = "**/relatorioProdCompradoNotaFiscal")
	public ResponseEntity<List<ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO>> relatorioProdCompradoNotaFiscal(@Valid @RequestBody ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO objetoRequisicaoRelatorioProdCompraNotaFiscaldto ) {
		
	
		List<ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO> retorno = 
				new ArrayList<ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO>();
		
		retorno = notaFiscalCompraService.gerarRelatorioProdCompraNota(objetoRequisicaoRelatorioProdCompraNotaFiscaldto);
		
		return new ResponseEntity<List<ObjetoRequisicaoRelatorioProdCompraNotaFiscalDTO>>(retorno, HttpStatus.OK);
	
	}
	
	
	
	
	
	@ResponseBody
	@PostMapping(value = "**/salvarNotaFiscalCompra")
	public ResponseEntity<NotaFiscalCompra> salvarNotaFiscalCompra(@RequestBody @Valid NotaFiscalCompra notaFiscalCompra)
			throws ExceptionLojaVirtual {// Recebe o json e converte para objeto

		if (notaFiscalCompra.getId() == null) {
			
			if (notaFiscalCompra.getDescricaoObs() != null) {
				List<NotaFiscalCompra> fiscalCompras = notaFiscalCompraRepository.buscarNotaDesc(notaFiscalCompra.getDescricaoObs().toUpperCase().trim());
				if(!fiscalCompras.isEmpty()) {
					throw new ExceptionLojaVirtual("Já existe Nota de compra com essa mesma descrição: " + notaFiscalCompra.getDescricaoObs());
				}
			
			}
			
		}
		
		
		if (notaFiscalCompra.getPessoa() == null || notaFiscalCompra.getPessoa().getId() <= 0) {
			throw new ExceptionLojaVirtual("Pessoa Juridica da nota Fiscal deve ser informada");
		}
		
		if(notaFiscalCompra.getEmpresa() == null || notaFiscalCompra.getEmpresa().getId() <= 0) {
			throw new ExceptionLojaVirtual("A empresa responsável  deve ser informada");
		}
		
		if(notaFiscalCompra.getContaPagar() == null || notaFiscalCompra.getContaPagar().getId() <= 0) {
			throw new ExceptionLojaVirtual("A Conta a Pagar da nota deve ser informada");

		}
		

		NotaFiscalCompra notaFiscalCompraSalvo = notaFiscalCompraRepository.save(notaFiscalCompra);

		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompraSalvo, HttpStatus.OK);
	}
	
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteNotaFiscalCompraPorId/{id}")
	public ResponseEntity<?> deleteNotaFiscalCompraPorId(@PathVariable("id") Long id) {

		notaFiscalCompraRepository.deleteItemNotaFiscalCompra(id); // deleta os filhos
		
		notaFiscalCompraRepository.deleteById(id); // deleta o pai

		return new ResponseEntity("Nota Fiscal Removida", HttpStatus.OK);

	}
	
	
	
	@ResponseBody
	@GetMapping(value = "**/obterNotaFiscalCompra/{id}")
	public ResponseEntity<NotaFiscalCompra> obterNotaFiscalCompra(@PathVariable("id") Long id) throws ExceptionLojaVirtual {

		NotaFiscalCompra notaFiscalCompra = notaFiscalCompraRepository.findById(id).orElse(null);

		if (notaFiscalCompra == null) {
			throw new ExceptionLojaVirtual("Não encontrou Nota Fiscal  com código: " + id);
		}

		return new ResponseEntity<NotaFiscalCompra>(notaFiscalCompra, HttpStatus.OK);

	}
	
	
	@ResponseBody
	@GetMapping(value = "**/obterNotaFiscalCompraDaVenda/{idVenda}")
	public ResponseEntity<List<NotaFiscalVendaDTO>>obterNotaFiscalCompraDaVenda(@PathVariable("idVenda") Long idVenda) throws ExceptionLojaVirtual {

		List<NotaFiscalVenda> notas = notaIFiscalVendaRepository.buscaNotaPorVenda(idVenda);

		if (notas == null || notas.isEmpty()) {
			throw new ExceptionLojaVirtual("Não encontrou Nota Fiscal venda  com código: " + idVenda);
		}
		
		List<NotaFiscalVendaDTO> dtos = notas.stream().map(NotaFiscalVendaDTO::fromEntity).toList();
		
		return new ResponseEntity<List<NotaFiscalVendaDTO>>(dtos, HttpStatus.OK);

	}
	
	@ResponseBody
	@GetMapping(value = "**/obterNotaFiscalCompraDaVendaUnica/{idVenda}")
	public ResponseEntity<NotaFiscalVendaDTO>obterNotaFiscalCompraDaVendaUnica(@PathVariable("idVenda") Long idVenda) throws ExceptionLojaVirtual {

		NotaFiscalVenda notas = notaIFiscalVendaRepository.buscaNotaPorVendaUnica(idVenda);

		if (notas == null) {
			throw new ExceptionLojaVirtual("Não encontrou Nota Fiscal venda  com código: " + idVenda);
		}
		
		NotaFiscalVendaDTO dtos = NotaFiscalVendaDTO.fromEntity(notas);


		return new ResponseEntity<NotaFiscalVendaDTO>(dtos, HttpStatus.OK);

	}
	
	
	@ResponseBody
	@GetMapping(value = "**/buscarNotaFiscalPorDesc/{desc}")
	public ResponseEntity<List<NotaFiscalCompra>> buscarNotaFiscalPorDesc(@PathVariable("desc") String desc) {

		List<NotaFiscalCompra> notaFiscalCompras = notaFiscalCompraRepository.buscarNotaDesc(desc.toUpperCase().trim());

		return new ResponseEntity<List<NotaFiscalCompra>>(notaFiscalCompras, HttpStatus.OK);

	}
	
	
	
}
