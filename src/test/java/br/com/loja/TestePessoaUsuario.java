package br.com.loja;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.loja.model.PessoaFisica;
import br.com.loja.model.PessoaJuridica;
import br.com.loja.repository.PessoaRepository;
import br.com.loja.service.PessoaUserService;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaUserService pessoaUserService; 
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	public void testeCadPessoaFisica() {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		
		pessoaJuridica.setCnpj("32123221231313110");
		pessoaJuridica.setNome("Tiago");
		pessoaJuridica.setEmail("tiago@gmail.com");
		pessoaJuridica.setTelefone("9999919191");
		pessoaJuridica.setInscrEstadual("9191831831919");
		pessoaJuridica.setInscrMunicipal("39391031010");
		pessoaJuridica.setNomeFantasia("101391391910310");
		pessoaJuridica.setRazaoSocial("1091991393910");
	
		pessoaRepository.save(pessoaJuridica);
		
		
		/*
		PessoaFisica pessoaFisica = new PessoaFisica();
		
		pessoaFisica.setCpf("32123221230");
		pessoaFisica.setNome("Tiago");
		pessoaFisica.setEmail("tiago@gmail.com");
		pessoaFisica.setTelefone("9999919191");
		pessoaFisica.setEmpresa(pessoaFisica);		
		*/
		
	}
	
	
}
