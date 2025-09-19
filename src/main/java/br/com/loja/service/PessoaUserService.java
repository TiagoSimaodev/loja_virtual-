package br.com.loja.service;

import java.util.Calendar;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.loja.dto.CepDTO;
import br.com.loja.model.PessoaFisica;
import br.com.loja.model.PessoaJuridica;
import br.com.loja.model.Usuario;
import br.com.loja.repository.PessoaFisicaRepository;
import br.com.loja.repository.PessoaRepository;
import br.com.loja.repository.UsuarioRepository;

@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	public PessoaJuridica salvarPessoaJuridica(PessoaJuridica juridica) {
		
		//juridica = pessoaRepository.save(juridica);
		
		for(int i = 0; i< juridica.getEnderecos().size(); i++) {
			juridica.getEnderecos().get(i).setPessoa(juridica);
			juridica.getEnderecos().get(i).setEmpresa(juridica);
			
			
		}
		
		juridica = pessoaRepository.save(juridica);
		
		Usuario usuarioPj = usuarioRepository.findUserByPessoa(juridica.getId(), juridica.getEmail());
		
		
		if (usuarioPj == null) {
			 String constraint = usuarioRepository.consultaConstraintAcesso();
			 if(constraint != null) {
				 jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;" );
			 }
			 
			 usuarioPj = new Usuario();
			 usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
			 usuarioPj.setEmpresa(juridica);
			 usuarioPj.setPessoa(juridica);
			 usuarioPj.setLogin(juridica.getEmail());
			
			 String senha = "" + Calendar.getInstance().getTimeInMillis();
			 String senhaCript = new BCryptPasswordEncoder().encode(senha);
			 
			 usuarioPj.setSenha(senhaCript);
			 usuarioPj = usuarioRepository.save(usuarioPj);
			 
			 usuarioRepository.insereAcessoUser(usuarioPj.getId());
			 usuarioRepository.insereAcessoUserPj(usuarioPj.getId(), "ROLE_ADMIN");
			 
			 StringBuilder messagemHtml = new StringBuilder();
			 messagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>");
			 messagemHtml.append("<b>Login: </b> "+juridica.getEmail()+"<br/>");
			 messagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
			 messagemHtml.append("Obrigado!!");

			 try {
			 serviceSendEmail.enviarEmailHtml("Acesso gerado para loja virtual", messagemHtml.toString(), juridica.getEmail());
			 }catch (Exception e) {
				 e.printStackTrace();
			 }
		}
		
		return juridica;
		
	}

	public PessoaFisica salvarPessoaFisica(PessoaFisica pessoaFisica) {
		//juridica = pessoaRepository.save(juridica);
		
				for(int i = 0; i< pessoaFisica.getEnderecos().size(); i++) {
					pessoaFisica.getEnderecos().get(i).setPessoa(pessoaFisica);
					//pessoaFisica.getEnderecos().get(i).setEmpresa(pessoaFisica);
					
					
				}
				
				pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);
				
				Usuario usuarioPj = usuarioRepository.findUserByPessoa(pessoaFisica.getId(), pessoaFisica.getEmail());
				
				
				if (usuarioPj == null) {
					 String constraint = usuarioRepository.consultaConstraintAcesso();
					 if(constraint != null) {
						 jdbcTemplate.execute("begin; alter table usuarios_acesso drop constraint " + constraint + "; commit;" );
					 }
					 
					 usuarioPj = new Usuario();
					 usuarioPj.setDataAtualSenha(Calendar.getInstance().getTime());
					 usuarioPj.setEmpresa(pessoaFisica.getEmpresa());
					 usuarioPj.setPessoa(pessoaFisica);
					 usuarioPj.setLogin(pessoaFisica.getEmail());
					
					 String senha = "" + Calendar.getInstance().getTimeInMillis();
					 String senhaCript = new BCryptPasswordEncoder().encode(senha);
					 
					 usuarioPj.setSenha(senhaCript);
					 usuarioPj = usuarioRepository.save(usuarioPj);
					 
					 usuarioRepository.insereAcessoUser(usuarioPj.getId());
					 
					 StringBuilder messagemHtml = new StringBuilder();
					 messagemHtml.append("<b>Segue abaixo seus dados de acesso para a loja virtual</b>");
					 messagemHtml.append("<b>Login: </b> "+pessoaFisica.getEmail()+"<br/>");
					 messagemHtml.append("<b>Senha: </b>").append(senha).append("<br/><br/>");
					 messagemHtml.append("Obrigado!!");

					 try {
					 serviceSendEmail.enviarEmailHtml("Acesso gerado para loja virtual", messagemHtml.toString(), pessoaFisica.getEmail());
					 }catch (Exception e) {
						 e.printStackTrace();
					 }
				}
				
				return pessoaFisica;
	}
	
	
	public CepDTO consultaCep(String cep) {
		return new RestTemplate().getForEntity("https://viacep.com.br/ws/"+ cep +"/json/", CepDTO.class).getBody();
	}
	
	
	
	
	

	
	
}
