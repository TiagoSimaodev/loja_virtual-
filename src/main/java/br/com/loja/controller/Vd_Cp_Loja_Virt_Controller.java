package br.com.loja.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.ExceptionLojaVirtual;
import br.com.loja.dto.VendaCompraLojaVirtualDTO;
import br.com.loja.model.Endereco;
import br.com.loja.model.PessoaFisica;
import br.com.loja.model.VendaCompraLojaVirtual;
import br.com.loja.repository.EnderecoRepository;
import br.com.loja.repository.NotaIFiscalVendaRepository;
import br.com.loja.repository.PessoaFisicaRepository;
import br.com.loja.repository.Vd_Cp_loja_virtual_repository;

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
	
	
	@ResponseBody
	@PostMapping(value = "**/salvarVendaLoja")
	public ResponseEntity<VendaCompraLojaVirtualDTO> salvarVendaLoja(@RequestBody @Valid VendaCompraLojaVirtual vendaCompraLojaVirtual) throws ExceptionLojaVirtual {
		
		
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
		
		//associa a nota fiscal a empresa
		vendaCompraLojaVirtual.getNotaFiscalVenda().setEmpresa(vendaCompraLojaVirtual.getEmpresa());

		// salva primeiro a venda e todos os dados
		VendaCompraLojaVirtual salvarVendaLojaSalvo = vd_Cp_loja_virtual_repository.saveAndFlush(vendaCompraLojaVirtual);
		
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
		
		return new ResponseEntity<VendaCompraLojaVirtualDTO>(compraLojaVirtualDTO, HttpStatus.CREATED);
		
		
	}
	
		
	
}
