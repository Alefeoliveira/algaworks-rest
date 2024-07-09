package br.com.alefeoliveira.api.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alefeoliveira.api.model.GrupoInput;
import br.com.alefeoliveira.api.model.PedidoInput;
import br.com.alefeoliveira.api.model.ProdutoInput;
import br.com.alefeoliveira.domain.model.Grupo;
import br.com.alefeoliveira.domain.model.Pedido;
import br.com.alefeoliveira.domain.model.Produto;

@Component
public class PedidoInputDisassembler {

	@Autowired
    private ModelMapper modelMapper;
    
    public Pedido toDomainObject(PedidoInput pedidoInput) {
        return modelMapper.map(pedidoInput, Pedido.class);
    }
    
    public void copyToDomainObject(PedidoInput pedidoInput, Pedido pedido) {
        modelMapper.map(pedidoInput, pedido);
    }  
}  