package br.com.alefeoliveira.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EstadoDTO {
	private Long id;
	@NotBlank
	private String nome;
}
