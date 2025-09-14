package br.com.loja.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.loja.model.Usuario;
import br.com.loja.repository.UsuarioRepository;

@Service
public class TarefasAutomatizadasService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ServiceSendEmail serviceSendEmail;
	
	
	// existem duas maneiras para automatizar essa tarefa, utilizando, initialDelay e fixedDelay
	// a 2 opção seria utilizadno cron 
	//@Scheduled(initialDelay = 2000, fixedDelay = 86400000) // Roda a cada 24 horas.
	@Scheduled(cron =  "0 0 11 * * *", zone ="America/Sao_Paulo")// vai rodar todo dia as 11 horas da manhã, horário de brasilia.
	public void notificarUserTrocaSenha() throws UnsupportedEncodingException, MessagingException, InterruptedException {
	
		List<Usuario> usuarios = usuarioRepository.usuarioSenhaVencida();
		
		for (Usuario usuario : usuarios) {
			
			StringBuilder msg = new StringBuilder();
			msg.append("Olá, ").append(usuario.getPessoa().getNome()).append("<br/>");
			msg.append("Sua senha ultrapassou o prazo de 90 dias, por favor altere sua senha.").append("<br/>");
			msg.append("aceso o menu de login e faça alteração em caso de duvidar entrar em contato com o suporte de atendimento.");
			
			serviceSendEmail.enviarEmailHtml("Troca de senha", msg.toString(), usuario.getLogin());
			
			Thread.sleep(3000);
			
		}
		
		
	}
	
	
	
	
}
