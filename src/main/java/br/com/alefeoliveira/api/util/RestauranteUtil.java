package br.com.alefeoliveira.api.util;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alefeoliveira.api.model.RestauranteDTO;
import br.com.alefeoliveira.domain.model.Cidade;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.model.Restaurante;

@Component
public class RestauranteUtil {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public RestauranteDTO toDTO(Restaurante restaurante) {
		return modelMapper.map(restaurante, RestauranteDTO.class);
	}
	
	public List<RestauranteDTO> toCollectionDTO(List<Restaurante> restaurantes){
		return restaurantes.stream().map(restaurante -> toDTO(restaurante)).collect(Collectors.toList());
	}
	
	public Restaurante toRestaurante(RestauranteDTO dto) {
		return modelMapper.map(dto, Restaurante.class);
	}
	
	public void copyToDomainObject(RestauranteDTO restauranteDTO, Restaurante restaurante) {
		//evitar erro
		restaurante.setCozinha(new Cozinha());
		
		if (restaurante.getEndereco() != null) {
			restaurante.getEndereco().setCidade(new Cidade());
		}
		
		modelMapper.map(restauranteDTO, restaurante);
	}

}
