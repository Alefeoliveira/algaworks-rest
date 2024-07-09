package br.com.alefeoliveira.core.modelMapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.alefeoliveira.api.model.EnderecoDTO;
import br.com.alefeoliveira.api.model.ItemPedidoInput;
import br.com.alefeoliveira.api.model.RestauranteDTO;
import br.com.alefeoliveira.domain.model.Endereco;
import br.com.alefeoliveira.domain.model.ItemPedido;
import br.com.alefeoliveira.domain.model.Restaurante;

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		
		//modelMapper.createTypeMap(Restaurante.class, RestauranteDTO.class)
		//.addMapping(Restaurante::getTaxaFrete, RestauranteDTO::setPrecoFrete);
		
		var enderecoToEndereciTypeMap = modelMapper.createTypeMap(Endereco.class, EnderecoDTO.class);
		enderecoToEndereciTypeMap.<String>addMapping(
				src -> src.getCidade().getEstado().getNome(), 
				(dest, value) -> dest.getCidade().setEstado(value));
		
		modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
	    .addMappings(mapper -> mapper.skip(ItemPedido::setId));  
		
		return new ModelMapper();
	}
}
