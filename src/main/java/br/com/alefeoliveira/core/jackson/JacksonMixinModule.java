package br.com.alefeoliveira.core.jackson;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleModule;

import br.com.alefeoliveira.domain.model.Cidade;
import br.com.alefeoliveira.domain.model.CidadeMixin;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.model.CozinhaMixin;
import br.com.alefeoliveira.domain.model.Restaurante;
import br.com.alefeoliveira.domain.model.RestauranteMixin;

@Component
public class JacksonMixinModule extends SimpleModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JacksonMixinModule() {
		setMixInAnnotation(Cozinha.class, CozinhaMixin.class);
		setMixInAnnotation(Cidade.class, CidadeMixin.class);
	}

}
