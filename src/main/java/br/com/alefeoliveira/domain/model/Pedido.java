package br.com.alefeoliveira.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import br.com.alefeoliveira.domain.enumerador.StatusPedido;
import br.com.alefeoliveira.domain.event.PedidoCanceladoEvent;
import br.com.alefeoliveira.domain.event.PedidoConfirmadoEvent;
import br.com.alefeoliveira.domain.exception.NegocioException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Pedido extends AbstractAggregateRoot<Pedido> {

	 	@EqualsAndHashCode.Include
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 	
	 	private String codigo;
	    
	    private BigDecimal subtotal;
	    private BigDecimal taxaFrete;
	    private BigDecimal valorTotal;

	    @Embedded
	    private Endereco enderecoEntrega;
	    
	    @Enumerated(EnumType.STRING)
	    private StatusPedido status = StatusPedido.CRIADO;
	    
	    @CreationTimestamp
	    private OffsetDateTime dataCriacao;

	    private OffsetDateTime dataConfirmacao;
	    private OffsetDateTime dataCancelamento;
	    private OffsetDateTime dataEntrega;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(nullable = false)
	    private FormaPagamento formaPagamento;
	    
	    @ManyToOne
	    @JoinColumn(nullable = false)
	    private Restaurante restaurante;
	    
	    @ManyToOne
	    @JoinColumn(name = "usuario_cliente_id", nullable = false)
	    private Usuario cliente;
	    
	    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	    private List<ItemPedido> itens = new ArrayList<>();  
	    
	    public void calcularValorTotal() {
	        getItens().forEach(ItemPedido::calcularPrecoTotal);
	        
	        this.subtotal = getItens().stream()
	            .map(item -> item.getPrecoTotal())
	            .reduce(BigDecimal.ZERO, BigDecimal::add);
	        
	        this.valorTotal = this.subtotal.add(this.taxaFrete);
	    }

	    public void definirFrete() {
	        setTaxaFrete(getRestaurante().getTaxaFrete());
	    }

	    public void atribuirPedidoAosItens() {
	        getItens().forEach(item -> item.setPedido(this));
	    }
	    
	    public void confirmar() {
	    	setStatus(StatusPedido.CONFIRMADO);
	    	setDataConfirmacao(OffsetDateTime.now());
	    	
	    	registerEvent(new PedidoConfirmadoEvent(this));
	    }
	    
	    public void entregar() {
	    	setStatus(StatusPedido.ENTREGUE);
	    	setDataEntrega(OffsetDateTime.now());
	    }
	    
	    public void cancelar() {
	        setStatus(StatusPedido.CANCELADO);
	        setDataCancelamento(OffsetDateTime.now());
	        
	        registerEvent(new PedidoCanceladoEvent(this));
	    }
	    
	    private void setStatus(StatusPedido novoStatus) {
	    	if(getStatus().naoPodeAlterarPara(novoStatus)) {
	    		throw new NegocioException(String.format("Status do pedido %s não pode sert alterado de %s para %s", 
						getCodigo(), getStatus().getDescricao(), novoStatus));
	    	}
	    	this.status = novoStatus;
	    }
	    
	    @PrePersist
	    private void gerarCodigo() {
	    	setCodigo(UUID.randomUUID().toString());
	    }
}
