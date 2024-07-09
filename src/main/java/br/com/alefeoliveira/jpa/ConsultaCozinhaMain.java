package br.com.alefeoliveira.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import br.com.alefeoliveira.AlgafoodApiApplication;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.repository.CozinhaRepository;

public class ConsultaCozinhaMain {
	
	public static void main(String[] args) {
		ApplicationContext appContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		CozinhaRepository cadastroCozinha = appContext.getBean(CozinhaRepository.class);
		
		List<Cozinha> lista = cadastroCozinha.findAll();
		lista.forEach(cozinha -> System.out.println(cozinha.getNome()));
		
		Optional<Cozinha> cozinha = cadastroCozinha.findById(1L);
		System.out.println(cozinha.get().getNome());
	}
}
