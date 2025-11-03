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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.ExceptionLojaVirtual;
import br.com.loja.dto.ItemVendaLojaDTO;
import br.com.loja.dto.ProdutoDTO;
import br.com.loja.dto.VendaCompraLojaVirtualDTO;
import br.com.loja.model.Endereco;
import br.com.loja.model.ItemVendaLoja;
import br.com.loja.model.PessoaFisica;
import br.com.loja.model.StatusRastreio;
import br.com.loja.model.VendaCompraLojaVirtual;
import br.com.loja.repository.EnderecoRepository;
import br.com.loja.repository.NotaIFiscalVendaRepository;
import br.com.loja.repository.StatusRastreioRepository;
import br.com.loja.repository.Vd_Cp_loja_virtual_repository;
import br.com.loja.service.VendaService;

@RestController
public class Vd_Cp_Loja_Virt_Controller {

	@Autowired
	private Vd_Cp_loja_virtual_repository vd_Cp_loja_virtual_repository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private PessoaController pessoaController;

	@Autowired
	private NotaIFiscalVendaRepository notaIFiscalVendaRepository;
	
	@Autowired
	private StatusRastreioRepository statusRastreioRepository;
	
	@Autowired
	private VendaService vendaService;

	@ResponseBody
	@PostMapping(value = "**/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(
			@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaVirtual {

		vendaCompraLojaVirtual.getPessoa().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		PessoaFisica pessoaFisica = pessoaController.salvarPf(vendaCompraLojaVirtual.getPessoa()).getBody();
		vendaCompraLojaVirtual.setPessoa(pessoaFisica);

		vendaCompraLojaVirtual.getEnderecoCobranca().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoCobranca().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoCobranca = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoCobranca());
		vendaCompraLojaVirtual.setEnderecoCobranca(enderecoCobranca);

		vendaCompraLojaVirtual.getEnderecoEntrega().setPessoa(pessoaFisica);
		vendaCompraLojaVirtual.getEnderecoEntrega().setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		Endereco enderecoEntrega = enderecoRepository.save(vendaCompraLojaVirtual.getEnderecoEntrega());
		vendaCompraLojaVirtual.setEnderecoEntrega(enderecoEntrega);

		// associa a nota fiscal a empresa
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

		for (int i = 0; i < vendaCompraLojaVirtual.getItemVendaLojas().size(); i++) {
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setEmpresa(vendaCompraLojaVirtual.getEmpresa());
			vendaCompraLojaVirtual.getItemVendaLojas().get(i).setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		}

		// salva primeiro a venda e todos os dados
		VendaCompraLojaVirtual salvarVendaLojaSalvo = vd_Cp_loja_virtual_repository
				.saveAndFlush(vendaCompraLojaVirtual);
		
		StatusRastreio statusRastreio = new StatusRastreio();
		statusRastreio.setCentroDistribuicao("Loja Local");
		statusRastreio.setCidade("local");
		statusRastreio.setEmpresa(vendaCompraLojaVirtual.getEmpresa());
		statusRastreio.setEstado("local");
		statusRastreio.setStatus("Inicio compra");
		statusRastreio.setVendaCompraLojaVirtual(vendaCompraLojaVirtual);
		
		statusRastreioRepository.save(statusRastreio);
		
		// Associa a venda gravada no banco com a nota fiscal
		vendaCompraLojaVirtual.getNotaFiscalVenda().setVendaCompraLojaVirtual(vendaCompraLojaVirtual);

		// Persiste novamente a nota fiscal novamente para ficar amarrada na venda
		notaIFiscalVendaRepository.saveAndFlush(vendaCompraLojaVirtual.getNotaFiscalVenda());
		salvarVendaLojaSalvo = vd_Cp_loja_virtual_repository.saveAndFlush(vendaCompraLojaVirtual);

		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();
		compraLojaVirtualDTO.setValorTotal(vendaCompraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(vendaCompraLojaVirtual.getPessoa());

		compraLojaVirtualDTO.setCobranca(vendaCompraLojaVirtual.getEnderecoCobranca());
		compraLojaVirtualDTO.setEntrega(vendaCompraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setValorDesc(vendaCompraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(vendaCompraLojaVirtual.getValorFrete());
		compraLojaVirtualDTO.setId(vendaCompraLojaVirtual.getId());

		for (ItemVendaLoja item : vendaCompraLojaVirtual.getItemVendaLojas()) {
			ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
			itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
			ProdutoDTO produtoDTO = new ProdutoDTO();
			
			produtoDTO.setId(item.getProduto().getId());
			produtoDTO.setNome(item.getProduto().getNome());
			produtoDTO.setDescricao(item.getProduto().getDescricao());
			produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
			produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
			itemVendaLojaDTO.setProduto(produtoDTO);

			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

		}

		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.CREATED);

	}

	@ResponseBody
	@GetMapping(value = "**/consultaVendaId/{id}")
	public ResponseEntity<VendaCompraLojaVirtualDTO> consultaVendaId(@PathVariable("id") Long idVenda) {

		VendaCompraLojaVirtual compraLojaVirtual = vd_Cp_loja_virtual_repository.findByIdExclusao(idVenda);

		if (compraLojaVirtual == null) {
			compraLojaVirtual = new VendaCompraLojaVirtual();
		}
		

		VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

		compraLojaVirtualDTO.setValorTotal(compraLojaVirtual.getValorTotal());
		compraLojaVirtualDTO.setPessoa(compraLojaVirtual.getPessoa());

		compraLojaVirtualDTO.setCobranca(compraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setEntrega(compraLojaVirtual.getEnderecoEntrega());
		compraLojaVirtualDTO.setValorDesc(compraLojaVirtual.getValorDesconto());
		compraLojaVirtualDTO.setValorFrete(compraLojaVirtualDTO.getValorFrete());
		compraLojaVirtualDTO.setId(compraLojaVirtual.getId());

		for (ItemVendaLoja item : compraLojaVirtual.getItemVendaLojas()) {
			ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
			itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
			ProdutoDTO produtoDTO = new ProdutoDTO();
			
			produtoDTO.setId(item.getProduto().getId());
			produtoDTO.setDescricao(item.getProduto().getDescricao());
			produtoDTO.setNome(item.getProduto().getNome());
			produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
			produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
			itemVendaLojaDTO.setProduto(produtoDTO);

			compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

		}

		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.OK);

	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteVendaTotalBanco/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBanco(@PathVariable( value = "idVenda") Long idVenda) {
		vendaService.exclusaoTotalVendaBanco(idVenda);
		
		return new ResponseEntity<String>("Venda excluida com sucesso!",HttpStatus.OK);
	}
	
	@ResponseBody
	@DeleteMapping(value = "**/deleteVendaTotalBancoLogica/{idVenda}")
	public ResponseEntity<String> deleteVendaTotalBancoLogica(@PathVariable( value = "idVenda") Long idVenda) {
		vendaService.exclusaoTotalVendaBancoLogica(idVenda);
		
		return new ResponseEntity<String>("Venda excluida logicamente com sucesso!",HttpStatus.OK);
	}

	@ResponseBody
	@PutMapping(value = "**/ativaRegistroVendaBanco/{idVenda}")
	public ResponseEntity<String> ativaRegistroVendaBanco(@PathVariable( value = "idVenda") Long idVenda) {
		vendaService.ativaRegistroVendaBanco(idVenda);
		
		return new ResponseEntity<String>("Venda ativada com sucesso!",HttpStatus.OK);
	}
	
	
	
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaDinamica/{valor}/{tipoconsulta}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> 
	consultaVendaDinamica(@PathVariable("valor") String valor, @PathVariable("tipoconsulta") String tipoconsulta) {

		List<VendaCompraLojaVirtual> compraLojaVirtual = null; 
		
		if (tipoconsulta.equalsIgnoreCase("POR_ID_PROD")) {
			compraLojaVirtual =vd_Cp_loja_virtual_repository.vendaPorProduto(Long.parseLong(valor));
		
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_PROD")) {
			compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorNomeProduto(valor.toUpperCase().trim());
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_CLIENTE")) {
			compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorNomeCliente(valor.toUpperCase().trim());
		}else if (tipoconsulta.equalsIgnoreCase("POR_NOME_ENDERECO_COBRANCA")) {
			compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorEnderecoCobranca(valor.toUpperCase().trim());
		}
		
		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vel : compraLojaVirtual) {
			
		

			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			compraLojaVirtualDTO.setValorTotal(vel.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vel.getPessoa());

			compraLojaVirtualDTO.setCobranca(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setEntrega(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesc(vel.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vel.getValorFrete());
			compraLojaVirtualDTO.setId(vel.getId());

			for (ItemVendaLoja item : vel.getItemVendaLojas()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
				ProdutoDTO produtoDTO = new ProdutoDTO();
			
				produtoDTO.setId(item.getProduto().getId());
				produtoDTO.setDescricao(item.getProduto().getDescricao());
				produtoDTO.setNome(item.getProduto().getNome());
				produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
				produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
				itemVendaLojaDTO.setProduto(produtoDTO);

				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

					}
				compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
				
			}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);

	}
	
	
	
	@ResponseBody
	@GetMapping(value = "**/consultaVendaPorProdutoId/{id}")
	public ResponseEntity<List<VendaCompraLojaVirtualDTO>> consultaVendaPorProdutoId(@PathVariable("id") Long idProd) {

		List<VendaCompraLojaVirtual> compraLojaVirtual = vd_Cp_loja_virtual_repository.vendaPorProduto(idProd);

		if (compraLojaVirtual == null) {
			compraLojaVirtual = new ArrayList<VendaCompraLojaVirtual>();
		}
		
		List<VendaCompraLojaVirtualDTO> compraLojaVirtualDTOList = new ArrayList<VendaCompraLojaVirtualDTO>();
		
		for (VendaCompraLojaVirtual vel : compraLojaVirtual) {
			
		

			VendaCompraLojaVirtualDTO compraLojaVirtualDTO = new VendaCompraLojaVirtualDTO();

			compraLojaVirtualDTO.setValorTotal(vel.getValorTotal());
			compraLojaVirtualDTO.setPessoa(vel.getPessoa());

			compraLojaVirtualDTO.setCobranca(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setEntrega(vel.getEnderecoEntrega());
			compraLojaVirtualDTO.setValorDesc(vel.getValorDesconto());
			compraLojaVirtualDTO.setValorFrete(vel.getValorFrete());
			compraLojaVirtualDTO.setId(vel.getId());

			for (ItemVendaLoja item : vel.getItemVendaLojas()) {
				ItemVendaLojaDTO itemVendaLojaDTO = new ItemVendaLojaDTO();
				itemVendaLojaDTO.setQuantidade(item.getQuantidade());
			
				ProdutoDTO produtoDTO = new ProdutoDTO();
			
				produtoDTO.setId(item.getProduto().getId());
				produtoDTO.setDescricao(item.getProduto().getDescricao());
				produtoDTO.setNome(item.getProduto().getNome());
				produtoDTO.setTipoUnidade(item.getProduto().getTipoUnidade());
				produtoDTO.setValorVenda(item.getProduto().getValorVenda());
			
			
				itemVendaLojaDTO.setProduto(produtoDTO);

				compraLojaVirtualDTO.getItemVendaLoja().add(itemVendaLojaDTO);

					}
				compraLojaVirtualDTOList.add(compraLojaVirtualDTO);
				
			}

		return new ResponseEntity<List<VendaCompraLojaVirtualDTO>>(compraLojaVirtualDTOList, HttpStatus.OK);

	}
	

}
