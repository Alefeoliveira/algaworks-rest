package br.com.alefeoliveira.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import br.com.alefeoliveira.domain.event.PedidoCanceladoEvent;
import br.com.alefeoliveira.domain.model.Pedido;
import br.com.alefeoliveira.domain.service.EnvioEmailService;
import br.com.alefeoliveira.domain.service.EnvioEmailService.Mensagem;

@Component
public class NotificacaoClientePedidoCanceladoListener {

    @Autowired
    private EnvioEmailService envioEmail;
    
    @TransactionalEventListener
    public void aoCancelarPedido(PedidoCanceladoEvent event) {
        Pedido pedido = event.getPedido();
        
        var mensagem = Mensagem.builder()
        		.assunto(pedido.getRestaurante().getNome() + " - Pedido cancelado")
                .corpo("pedido-cancelado.html")
                .variavel("pedido", pedido)
                .destinatario(pedido.getCliente().getEmail())
                .build();

        envioEmail.enviar(mensagem);
    }   
} 
