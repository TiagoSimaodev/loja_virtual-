package br.com.loja;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.loja.controller.PessoaController;
import br.com.loja.model.PessoaFisica;
import br.com.loja.model.PessoaJuridica;
import br.com.loja.repository.PessoaRepository;
import br.com.loja.service.PessoaUserService;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	
	
	@Autowired
	private PessoaController pessoaController;
	
	@Test
	public void testeCadPessoaFisica() throws ExceptionLojaVirtual {
		
		PessoaJuridica pessoaJuridica = new PessoaJuridica();
		
		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Tiago");
		pessoaJuridica.setEmail("tiagoTESTEPJ@gmail.com");
		pessoaJuridica.setTelefone("9999919191");
		pessoaJuridica.setInscrEstadual("9191831831919");
		pessoaJuridica.setInscrMunicipal("39391031010");
		pessoaJuridica.setNomeFantasia("101391391910310");
		pessoaJuridica.setRazaoSocial("1091991393910");
		
		pessoaController.salvarPj(pessoaJuridica);
	
	
	}
}