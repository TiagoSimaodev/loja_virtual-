package br.com.loja.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.loja.ExceptionLojaVirtual;
import br.com.loja.dto.CepDTO;
import br.com.loja.model.Endereco;
import br.com.loja.model.PessoaFisica;
import br.com.loja.model.PessoaJuridica;
import br.com.loja.repository.EnderecoRepository;
import br.com.loja.repository.PessoaFisicaRepository;
import br.com.loja.repository.PessoaRepository;
import br.com.loja.service.PessoaUserService;
import br.com.loja.util.ValidaCNPJ;
import br.com.loja.util.ValidaCPF;

@RestController
public class PessoaController {

	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaUserService pessoaUserService;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	
	@ResponseBody
	@GetMapping(value = "**/consultaPfNome/{nome}")
	public ResponseEntity<List<PessoaFisica>> consultaPfNome(@PathVariable("nome") String nome ) {
		
		List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorNomePf(nome.trim().toUpperCase());
		
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaPfCpf/{cpf}")
	public ResponseEntity<List<PessoaFisica>> consultaPfCpf(@PathVariable("cpf") String cpf ) {
		
		List<PessoaFisica> fisicas = pessoaFisicaRepository.pesquisaPorCpfPf(cpf);
		
		return new ResponseEntity<List<PessoaFisica>>(fisicas, HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/consultaNomePj/{nome}")
	public ResponseEntity<List<PessoaJuridica>> consultaNomePj(@PathVariable("nome") String nome ) {
		
		List<PessoaJuridica> fisicas = pessoaRepository.pesquisaPorNomePj(nome.trim().toUpperCase());
		
		return new ResponseEntity<List<PessoaJuridica>>(fisicas, HttpStatus.OK);
		
	}
	
	
	@ResponseBody
	@GetMapping(value = "**/consultaCnpjPj/{cnpj}")
	public ResponseEntity<List<PessoaJuridica>> consultaCnpjPj(@PathVariable("nome") String cnpj ) {
		
		List<PessoaJuridica> fisicas = pessoaRepository.existeCnpjCadastradoList(cnpj.trim().toUpperCase());
		
		return new ResponseEntity<List<PessoaJuridica>>(fisicas, HttpStatus.OK);
		
	}
	
	@ResponseBody
	@GetMapping(value = "**/consultaCep/{cep}")
	public ResponseEntity<CepDTO> consultaCep(@PathVariable("cep") String cep){
		
		return new ResponseEntity<CepDTO>( pessoaUserService.consultaCep(cep), HttpStatus.OK);
	
	}
	
	
	
	
	
	//endpoint é microservicoe é um api
	@ResponseBody
	@PostMapping(value = "**/salvarPj")
	public ResponseEntity<PessoaJuridica> salvarPj(@RequestBody @Valid PessoaJuridica pessoaJuridica) throws ExceptionLojaVirtual {
		
		if (pessoaJuridica == null) {
			throw new ExceptionLojaVirtual("Pessoa juridica não pode ser Null");
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeCnpjCadastrado(pessoaJuridica.getCnpj()) != null ) {
			throw new ExceptionLojaVirtual("Já existe um CNPJ cadastrado com o numero: " + pessoaJuridica.getCnpj());
		}
		
		if(pessoaJuridica.getId() == null && pessoaRepository.existeInscEstadualCadastrado(pessoaJuridica.getInscrEstadual()) != null ) {
			throw new ExceptionLojaVirtual("Já existe Inscrição estadual cadastradoa com o numero: " + pessoaJuridica.getInscrEstadual());
		}
		
		
		if(!ValidaCNPJ.isCNPJ(pessoaJuridica.getCnpj())) {
			throw new ExceptionLojaVirtual("Cnpj: " + pessoaJuridica.getCnpj() + " Está inválido.");
		}
		
		if (pessoaJuridica.getId() == null || pessoaJuridica.getId() <= 0) {
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				
				CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
				
				pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
				pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
				pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
				pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
				pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
			
			}
		}else {
			
			for (int p = 0; p < pessoaJuridica.getEnderecos().size(); p++) {
				
				Endereco enderecoTemp = enderecoRepository.findById(pessoaJuridica.getEnderecos().get(p).getId()).get();
				
				if (!enderecoTemp.getCep().equals(pessoaJuridica.getEnderecos().get(p).getCep())) {
					
					CepDTO cepDTO = pessoaUserService.consultaCep(pessoaJuridica.getEnderecos().get(p).getCep());
					
					pessoaJuridica.getEnderecos().get(p).setBairro(cepDTO.getBairro());
					pessoaJuridica.getEnderecos().get(p).setCidade(cepDTO.getLocalidade());
					pessoaJuridica.getEnderecos().get(p).setComplemento(cepDTO.getComplemento());
					pessoaJuridica.getEnderecos().get(p).setRuaLogra(cepDTO.getLogradouro());
					pessoaJuridica.getEnderecos().get(p).setUf(cepDTO.getUf());
				
				}
				
			}
		}
		
		pessoaJuridica = pessoaUserService.salvarPessoaJuridica(pessoaJuridica);
		
		return new ResponseEntity<PessoaJuridica>(pessoaJuridica, HttpStatus.OK);
	}
	
	

	//endpoint é microservicoe é um api
	@ResponseBody
	@PostMapping(value = "**/salvarPf")
	public ResponseEntity<PessoaFisica> salvarPf(@RequestBody PessoaFisica pessoaFisica) throws ExceptionLojaVirtual {
		
		if (pessoaFisica == null) {
			throw new ExceptionLojaVirtual("Pessoa Fisica não pode ser Null");
		}
		
		if(pessoaFisica.getId() == null && pessoaRepository.existeCpfCadastrado(pessoaFisica.getCpf()) != null ) {
			throw new ExceptionLojaVirtual("Já existe um CPF cadastrado com o numero: " + pessoaFisica.getCpf());
		}
		
		
		
		
		if(!ValidaCPF.isCPF(pessoaFisica.getCpf())) {
			throw new ExceptionLojaVirtual("CPF: " + pessoaFisica.getCpf() + " Está inválido.");
		}
		
		pessoaFisica = pessoaUserService.salvarPessoaFisica(pessoaFisica);
		
		return new ResponseEntity<PessoaFisica>(pessoaFisica, HttpStatus.OK);
	}
	
}
