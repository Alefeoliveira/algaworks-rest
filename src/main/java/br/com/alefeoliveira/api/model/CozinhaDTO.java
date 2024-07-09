package br.com.alefeoliveira.api.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CozinhaDTO {
	private Long id;
	
	@NotNull
	private String nome;
}
