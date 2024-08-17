package br.com.alefeoliveira.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alefeoliveira.api.model.CozinhaDTO;
import br.com.alefeoliveira.api.util.CozinhaModelAssembler;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.repository.CozinhaRepository;
import br.com.alefeoliveira.domain.service.CozinhaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository repository;
	
	@Autowired
	private CozinhaService service;
	
	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;
	
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
	@GetMapping
	public PagedModel<CozinhaDTO> listarCozinha(@PageableDefault(size = 10) Pageable pageable) {
		Page<Cozinha> cozinhasPage = repository.findAll(pageable);
		
		PagedModel<CozinhaDTO> cozinhasPagedModel = pagedResourcesAssembler
				.toModel(cozinhasPage, cozinhaModelAssembler);
		
		return cozinhasPagedModel;
	}
	
	@GetMapping("/por-nome")
	public List<Cozinha> listarCozinhaPorNome(@RequestParam String nome) {
		return repository.findByNomeContaining(nome);
	}
	
	
	@GetMapping("/{cozinhaId}")
	public Cozinha buscar(@PathVariable("cozinhaId") Long id) {
		return service.buscarOuFalhar(id);
	}
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Cozinha salvar(@RequestBody @Valid Cozinha cozinha){
		return repository.save(cozinha);
	}
	
	@PutMapping("/{id}")
	public Cozinha atualizar(@RequestBody  @Valid Cozinha cozinha, @PathVariable Long id){
		Cozinha cozinhaConsulta = service.buscarOuFalhar(id);
	
		BeanUtils.copyProperties(cozinha, cozinhaConsulta, "id");
		return service.salvar(cozinhaConsulta);
	}
	
	
	@DeleteMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id){
		service.excluir(id);
	}
	
}
