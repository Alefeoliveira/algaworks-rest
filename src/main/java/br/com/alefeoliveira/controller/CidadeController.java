package br.com.alefeoliveira.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.alefeoliveira.domain.exception.EntidadeEmUsoException;
import br.com.alefeoliveira.domain.exception.EntidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.EstadoNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.NegocioException;
import br.com.alefeoliveira.domain.model.Cidade;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.model.Estado;
import br.com.alefeoliveira.domain.repository.CozinhaRepository;
import br.com.alefeoliveira.domain.repository.EstadoRepository;
import br.com.alefeoliveira.domain.service.CidadeService;
import br.com.alefeoliveira.exceptionhandler.Problema;
import ch.qos.logback.core.joran.util.beans.BeanUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	private CidadeService service;
	
	@GetMapping
	public List<Cidade> listarCidades() {
		return service.buscarTodos();
	}
	
	@GetMapping("/{id}")
	public Cidade buscar(@PathVariable("id") Long id) {
		return service.buscar(id);
	}
	
	@PutMapping("/{id}")
	public Cidade atualizar(@PathVariable("id") Long id, @RequestBody @Valid Cidade cidade) {

		try {
			Cidade atual = service.buscar(id);
			BeanUtils.copyProperties(cidade, atual, "id");
			return service.atualizar(atual, id);
		}catch(EstadoNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id){
		service.excluir(id);
	}

}
