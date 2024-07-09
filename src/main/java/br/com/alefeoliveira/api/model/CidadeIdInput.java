package br.com.alefeoliveira.api.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CidadeIdInput {
	@NotNull
	private Long id;
}
