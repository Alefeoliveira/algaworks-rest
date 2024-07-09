package br.com.alefeoliveira.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import br.com.alefeoliveira.AlgafoodApiApplication;
import br.com.alefeoliveira.domain.model.Restaurante;
import br.com.alefeoliveira.domain.repository.RestauranteRepository;

public class ConsultaRestauranteMain {
	
	public static void main(String[] args) {
		ApplicationContext appContext = new SpringApplicationBuilder(AlgafoodApiApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
		RestauranteRepository restaurenteCadastro = appContext.getBean(RestauranteRepository.class);
		
		List<Restaurante> lista = restaurenteCadastro.findAll();
		lista.forEach(restaurante -> System.out.println(restaurante.getNome()));
		
		Optional<Restaurante> res = restaurenteCadastro.findById(1L);
		System.out.println(res.get().getNome());
	}
}
