package br.com.loja.service;

import java.util.Calendar;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.loja.model.PessoaJuridica;
import br.com.loja.model.Usuario;
import br.com.loja.repository.PessoaRepository;
import br.com.loja.repository.UsuarioRepository;

@Service
public class PessoaUserService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
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
			 
			 usuarioRepository.insereAcessoUserPj(usuarioPj.getId());
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
	
	

	
	
}
