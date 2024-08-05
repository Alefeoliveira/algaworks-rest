package br.com.alefeoliveira.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alefeoliveira.domain.enumerador.StatusPedido;
import br.com.alefeoliveira.domain.exception.NegocioException;
import br.com.alefeoliveira.domain.model.Pedido;
import br.com.alefeoliveira.domain.repository.PedidoRepository;
import br.com.alefeoliveira.domain.service.EnvioEmailService.Mensagem;
import jakarta.transaction.Transactional;

@Service
public class FluxoPedidoService {
	
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private PedidoRepository repo;
	
	@Transactional
	public void confirmar(String codigoPedido) {
		Pedido pedido = pedidoService.buscarOuFalhar(codigoPedido);
		pedido.confirmar();
		pedido.setDataConfirmacao(OffsetDateTime.now());
		
		repo.save(pedido);
	}
	


	@Transactional
	public void cancelar(String codigoPedido) {
	    Pedido pedido = pedidoService.buscarOuFalhar(codigoPedido);
	    pedido.cancelar();
	    
	    repo.save(pedido);
	}
	
	@Transactional
	public void entregar(String codigoPedido) {
	    Pedido pedido = pedidoService.buscarOuFalhar(codigoPedido);
	    
	    pedido.entregar();
	    pedido.setDataEntrega(OffsetDateTime.now());
	}
}
