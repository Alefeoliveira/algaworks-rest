package br.com.alefeoliveira.domain.event;

import br.com.alefeoliveira.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoCanceladoEvent {

    private Pedido pedido;
    
}