package br.com.alefeoliveira.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import br.com.alefeoliveira.core.config.EmailProperties;
import br.com.alefeoliveira.domain.exception.EmailException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;

public class SmtpEnvioEmailService implements EnvioEmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailProperties properties;
	
	@Autowired
	private Configuration freemarkerConfig;

	@Override
	public void enviar(Mensagem mensagem) {
	    try {
	        MimeMessage mimeMessage = criarMimeMessage(mensagem);
	        
	        mailSender.send(mimeMessage);
	    } catch (Exception e) {
	        throw new EmailException("Não foi possível enviar e-mail");
	    }
	}

	protected MimeMessage criarMimeMessage(Mensagem mensagem) throws Exception {
	    String corpo = processarTemplate(mensagem);
	    
	    MimeMessage mimeMessage = mailSender.createMimeMessage();
	    
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
	    helper.setFrom(properties.getRemetente());
	    helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
	    helper.setSubject(mensagem.getAssunto());
	    helper.setText(corpo, true);
	    
	    return mimeMessage;
	}
	
	protected String processarTemplate(Mensagem msg) throws Exception {
		Template template = freemarkerConfig.getTemplate(msg.getCorpo());
		
		return FreeMarkerTemplateUtils.processTemplateIntoString(template, msg.getVariaveis());
	}

}
