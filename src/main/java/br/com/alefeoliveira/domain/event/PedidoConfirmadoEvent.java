package br.com.alefeoliveira.domain.event;

import br.com.alefeoliveira.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PedidoConfirmadoEvent {

	private Pedido pedido;
}
