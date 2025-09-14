package br.com.loja;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.loja.controller.PessoaController;
import br.com.loja.enums.TipoEndereco;
import br.com.loja.model.Endereco;
import br.com.loja.model.PessoaFisica;
import br.com.loja.model.PessoaJuridica;
import br.com.loja.repository.PessoaRepository;
import br.com.loja.service.PessoaUserService;
import junit.framework.TestCase;

@SpringBootTest(classes = LojaVirtualApplication.class)
public class TestePessoaUsuario extends TestCase {

	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Test
	public void testeCadPessoaJuridica() throws ExceptionLojaVirtual {

		PessoaJuridica pessoaJuridica = new PessoaJuridica();

		pessoaJuridica.setCnpj("" + Calendar.getInstance().getTimeInMillis());
		pessoaJuridica.setNome("Tiago");
		pessoaJuridica.setEmail("tiagosimaodevtest@gmail.com");
		pessoaJuridica.setTelefone("9999919191");
		pessoaJuridica.setInscrEstadual("9191831831919");
		pessoaJuridica.setInscrMunicipal("39391031010");
		pessoaJuridica.setNomeFantasia("101391391910310");
		pessoaJuridica.setRazaoSocial("1091991393910");

		Endereco endereco1 = new Endereco();
		
		
		endereco1.setBairro("teste bairro");
		endereco1.setCep("28278717281");
		endereco1.setComplemento("Casa");
		endereco1.setEmpresa(pessoaJuridica);
		endereco1.setPessoa(pessoaJuridica);
		endereco1.setNumero("120");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("ce");
		endereco1.setRuaLogra("rua teste rua");
		endereco1.setCidade("fortaleza");
		
		Endereco endereco2 = new Endereco();

		endereco2.setBairro("teste");
		endereco2.setCep("2827871721211");
		endereco2.setComplemento("Apartamento");
		endereco2.setEmpresa(pessoaJuridica);
		endereco2.setPessoa(pessoaJuridica);
		endereco2.setNumero("1500");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("sp");
		endereco2.setRuaLogra("testando rua");
		endereco2.setCidade("fortaleza");
		
		pessoaJuridica.getEnderecos().add(endereco2);
		pessoaJuridica.getEnderecos().add(endereco1);
		
		pessoaJuridica =pessoaController.salvarPj(pessoaJuridica).getBody();
		
		assertEquals(true, pessoaJuridica.getId() > 0);
		
		for (Endereco endereco : pessoaJuridica.getEnderecos() ) {
			assertEquals(true,  endereco.getId() >0);
		}
		
		assertEquals(2, pessoaJuridica.getEnderecos().size());

	}
	
	
	@Test
	public void testeCadPessoaFisica() throws ExceptionLojaVirtual {

		PessoaJuridica pessoaJuridica =  pessoaRepository.existeCnpjCadastrado("1757648113300");
		
		PessoaFisica pessoaFisica = new PessoaFisica();

		pessoaFisica.setCpf("553.090.790-33");
		pessoaFisica.setNome("Tiago");
		pessoaFisica.setEmail("tiagossasadd1a@gmail.com");
		pessoaFisica.setTelefone("9999919191");
		pessoaFisica.setEmpresa(pessoaJuridica);

		Endereco endereco1 = new Endereco();
		
		
		endereco1.setBairro("teste bairro");
		endereco1.setCep("28278717281");
		endereco1.setComplemento("Casa");
		endereco1.setPessoa(pessoaFisica);
		endereco1.setNumero("120");
		endereco1.setTipoEndereco(TipoEndereco.COBRANCA);
		endereco1.setUf("ce");
		endereco1.setRuaLogra("rua teste rua");
		endereco1.setCidade("fortaleza");
		endereco1.setEmpresa(pessoaJuridica);
		
		
		Endereco endereco2 = new Endereco();

		endereco2.setBairro("teste");
		endereco2.setCep("2827871721211");
		endereco2.setComplemento("Apartamento");
		endereco2.setPessoa(pessoaFisica);
		endereco2.setNumero("1500");
		endereco2.setTipoEndereco(TipoEndereco.ENTREGA);
		endereco2.setUf("sp");
		endereco2.setRuaLogra("testando rua");
		endereco2.setCidade("fortaleza");
		endereco2.setEmpresa(pessoaJuridica);
		
		pessoaFisica.getEnderecos().add(endereco2);
		pessoaFisica.getEnderecos().add(endereco1);
		
		pessoaFisica =pessoaController.salvarPf(pessoaFisica).getBody();
		
		assertEquals(true, pessoaFisica.getId() > 0);
		
		for (Endereco endereco : pessoaFisica.getEnderecos() ) {
			assertEquals(true,  endereco.getId() >0);
		}
		
		assertEquals(2, pessoaFisica.getEnderecos().size());

	}
	
	
}