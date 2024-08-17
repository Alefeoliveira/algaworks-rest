package br.com.alefeoliveira.api.model;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import br.com.alefeoliveira.controller.CidadeController;
import br.com.alefeoliveira.controller.EstadoController;
import br.com.alefeoliveira.domain.model.Cidade;

@Component
public class CidadeModelAssembler extends RepresentationModelAssemblerSupport<Cidade, Cidade> {

	@Autowired
    private ModelMapper modelMapper;
	
    public CidadeModelAssembler() {
		super(CidadeController.class, Cidade.class);
	}

	@Override
	public Cidade toModel(Cidade entity) {
		
		Cidade cidade = createModelWithId(entity.getId(), entity);
		
		modelMapper.map(entity, cidade);
		
		//Cidade cidade = modelMapper.map(entity, Cidade.class);
		
		cidade.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class).buscar(cidade.getId())).withSelfRel());
		
		cidade.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CidadeController.class).listarCidades()).withRel("cidades"));

		cidade.getEstado().add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EstadoController.class)
				.buscar(cidade.getEstado().getId())).withSelfRel());
		
		return cidade;
	}
	
	
	@Override
	public CollectionModel<Cidade> toCollectionModel(Iterable<? extends Cidade> entities) {
		return super.toCollectionModel(entities)
				.add(WebMvcLinkBuilder.linkTo(CidadeController.class).withSelfRel());
	}
}