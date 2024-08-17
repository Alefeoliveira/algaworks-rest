package br.com.alefeoliveira.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alefeoliveira.api.model.CidadeModelAssembler;
import br.com.alefeoliveira.api.util.ResourceUriHelper;
import br.com.alefeoliveira.domain.exception.EstadoNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.NegocioException;
import br.com.alefeoliveira.domain.model.Cidade;
import br.com.alefeoliveira.domain.service.CidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cidades")
@Tag(name = "Cidades")
public class CidadeController {
	
	@Autowired
	private CidadeService service;
	
	@Autowired
	private CidadeModelAssembler assembler;
	
	@ApiResponses(value = @ApiResponse(responseCode = "200", description = "status 200 ok"))
	@GetMapping
	public CollectionModel<Cidade> listarCidades() {
		return assembler.toCollectionModel(service.buscarTodos());
		
		//CollectionModel<Cidade> cidadesCollectionModel = CollectionModel.of(cidadeList);
		
		//cidadesCollectionModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class).withSelfRel());
		
		//return cidadesCollectionModel;
	}
	
	@Operation(description = "Busca uma cidade por id")
	@GetMapping("/{id}")
	public Cidade buscar(@Parameter(description = "Id de uma cidade") @PathVariable("id") Long id) {
		Cidade cidade = service.buscar(id);
		
	
		//cidade.add(Link.of("http://localhost:8080/cidades/1", IanaLinkRelations.SELF));
		//cidade.add(Link.of("http://localhost:8080/cidades", IanaLinkRelations.COLLECTION));
		
		//cidade.getEstado().add(Link.of("http://localhost:8080/estados/1"));

	
		return assembler.toModel(cidade);
	}
	
	@Operation(description = "Cadastra uma cidade", summary = "Cadastra uma cidade")
	@PutMapping("/{id}")
	public Cidade atualizar(@Parameter(name = "corpo", description = "Representação de uma nova cidade") @PathVariable("id") Long id, @RequestBody @Valid Cidade cidade) {

		try {
			Cidade atual = service.buscar(id);
			BeanUtils.copyProperties(cidade, atual, "id");
			
			ResourceUriHelper.addUriInResponseHeader(atual.getId());
			
			return service.atualizar(atual, id);
		}catch(EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@Operation(description = "Exclui uma cidade por id")
	@DeleteMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id){
		service.excluir(id);
	}

}
