package br.com.alefeoliveira.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CidadeDTO {
	
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotNull
	private EstadoDTO estado;
}
