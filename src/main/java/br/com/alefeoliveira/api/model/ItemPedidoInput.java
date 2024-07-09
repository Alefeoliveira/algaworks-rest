package br.com.alefeoliveira.api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemPedidoInput {

	@NotNull
    private Long produtoId;
    
    @NotNull
    @PositiveOrZero
    private Integer quantidade;
    
    private String observacao;   
    
}      