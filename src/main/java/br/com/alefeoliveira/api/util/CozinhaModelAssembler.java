package br.com.alefeoliveira.api.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.alefeoliveira.api.model.CozinhaDTO;
import br.com.alefeoliveira.controller.CozinhaController;
import br.com.alefeoliveira.domain.model.Cozinha;

@Component
public class CozinhaModelAssembler extends RepresentationModelAssemblerSupport<Cozinha, CozinhaDTO> {

	@Autowired
    private ModelMapper modelMapper;
	
	public CozinhaModelAssembler() {
		super(CozinhaController.class, CozinhaDTO.class);
	}
    
	@Override
    public CozinhaDTO toModel(Cozinha cozinha) {
		CozinhaDTO dto = createModelWithId(cozinha.getId(), cozinha);
		modelMapper.map(cozinha, dto);
		
		dto.add(WebMvcLinkBuilder.linkTo(CozinhaController.class).withRel("cozinhas"));
		
        return modelMapper.map(cozinha, CozinhaDTO.class);
    }
}
