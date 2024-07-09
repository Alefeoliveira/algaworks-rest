package br.com.alefeoliveira.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SenhaInput {

	  @NotBlank
	    private String senhaAtual;
	    
	    @NotBlank
	    private String novaSenha;
    
}      