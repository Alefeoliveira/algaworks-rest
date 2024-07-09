package br.com.alefeoliveira.api.model;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class RestauranteDTO {
	private Long restauranteId;
	@NotBlank
	private String nome;
	@NotNull
	@PositiveOrZero
	private BigDecimal taxaFrete;
	@Valid
	@NotNull
	private CozinhaDTO cozinha;
	private Boolean ativo;
	private EnderecoDTO endereco;
	private Boolean aberto;
}
