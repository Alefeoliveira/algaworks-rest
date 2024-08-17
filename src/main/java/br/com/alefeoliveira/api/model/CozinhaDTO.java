package br.com.alefeoliveira.api.model;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.alefeoliveira.domain.model.view.RestauranteView;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CozinhaDTO extends RepresentationModel<CozinhaDTO>{
	@JsonView(RestauranteView.Resumo.class)
	private Long id;
	
	@NotNull
	@JsonView(RestauranteView.Resumo.class)
	private String nome;
}
