package br.com.alefeoliveira.api.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alefeoliveira.api.model.CozinhaDTO;
import br.com.alefeoliveira.api.model.PedidoModel;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.model.Pedido;

@Component
public class CozinhaModelAssembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public CozinhaDTO toModel(Cozinha cozinha) {
        return modelMapper.map(cozinha, CozinhaDTO.class);
    }
    
    public List<CozinhaDTO> toCollectionModel(List<Cozinha> cozinhas) {
        return cozinhas.stream()
                .map(cozinha -> toModel(cozinha))
                .collect(Collectors.toList());
    }
    
}
