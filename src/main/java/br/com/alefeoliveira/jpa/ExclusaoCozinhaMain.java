package br.com.alefeoliveira.jpa;

import java.util.List;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import br.com.alefeoliveira.AlgafoodApiApplication;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.repository.CozinhaRepository;

public class ExclusaoCozinhaMain {
	
	public static void main(String[] args) {
		ApplicationContext appContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		CozinhaRepository cadastroCozinha = appContext.getBean(CozinhaRepository.class);
		
		Cozinha cozinha1 = new Cozinha();
		cozinha1.setId(2L);
		
		cadastroCozinha.deleteById(cozinha1.getId());
	}
}
