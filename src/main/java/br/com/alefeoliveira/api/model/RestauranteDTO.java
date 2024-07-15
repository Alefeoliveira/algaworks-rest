package br.com.alefeoliveira.api.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonView;

import br.com.alefeoliveira.domain.model.view.RestauranteView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class RestauranteDTO {
	@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class})
	private Long restauranteId;
	@NotBlank
	@JsonView({RestauranteView.Resumo.class, RestauranteView.ApenasNome.class})
	private String nome;
	@NotNull
	@PositiveOrZero
	@JsonView(RestauranteView.Resumo.class)
	private BigDecimal taxaFrete;
	@Valid
	@NotNull
	@JsonView(RestauranteView.Resumo.class)
	private CozinhaDTO cozinha;
	private Boolean ativo;
	private EnderecoDTO endereco;
	private Boolean aberto;
}
