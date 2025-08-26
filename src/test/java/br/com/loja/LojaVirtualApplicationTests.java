package br.com.loja;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.loja.controller.AcessoController;
import br.com.loja.model.Acesso;
import br.com.loja.repository.AcessoRepository;
import br.com.loja.service.AcessoService;

@SpringBootTest(classes = LojaVirtualApplication.class)
public class LojaVirtualApplicationTests {
	
	@Autowired
	private AcessoService acessoService;

	@Autowired
	private AcessoController acessoController;
	
	//@Autowired
//	private AcessoRepository acessoRepository;
	
	
	@Test
	public void testCadastraAcesso() {
		Acesso acesso = new Acesso();
		acesso.setDescricao("ROLE_ADMIN");
		
		acessoController.salvarAcesso(acesso);
		
	}

}
