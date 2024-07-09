package br.com.alefeoliveira.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alefeoliveira.domain.enumerador.StatusPedido;
import br.com.alefeoliveira.domain.exception.NegocioException;
import br.com.alefeoliveira.domain.model.Pedido;
import jakarta.transaction.Transactional;

@Service
public class FluxoPedidoService {
	
	@Autowired
	private PedidoService pedidoService;
	
	@Transactional
	public void confirmar(String codigoPedido) {
		Pedido pedido = pedidoService.buscarOuFalhar(codigoPedido);
		
		pedido.confirmar();
		pedido.setDataConfirmacao(OffsetDateTime.now());
	}
	

	@Transactional
	public void cancelar(String codigoPedido) {
	    Pedido pedido = pedidoService.buscarOuFalhar(codigoPedido);

	    pedido.cancelar();
	    pedido.setDataCancelamento(OffsetDateTime.now());
	}
	
	@Transactional
	public void entregar(String codigoPedido) {
	    Pedido pedido = pedidoService.buscarOuFalhar(codigoPedido);
	    
	    pedido.entregar();
	    pedido.setDataEntrega(OffsetDateTime.now());
	}
}
