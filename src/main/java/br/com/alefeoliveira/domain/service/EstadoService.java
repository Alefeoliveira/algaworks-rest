package br.com.alefeoliveira.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.alefeoliveira.domain.exception.EntidadeEmUsoException;
import br.com.alefeoliveira.domain.exception.EntidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.EstadoNaoEncontradaException;
import br.com.alefeoliveira.domain.model.Cidade;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.model.Estado;
import br.com.alefeoliveira.domain.repository.CozinhaRepository;
import br.com.alefeoliveira.domain.repository.EstadoRepository;
import jakarta.transaction.Transactional;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repository;

	@Transactional
	public Estado salvar(Estado estado) {
		return repository.save(estado);
	}
	
	public Estado buscar(Long id) {
		return repository.findById(id).orElseThrow(() -> new EstadoNaoEncontradaException(id));
	}
	
	public List<Estado> buscarTodos() {
		return repository.findAll();
	}
	
	@Transactional
	public Estado atualizar(Estado estado, Long id) {
		Estado estadoConsultado = buscar(id);
		
		estadoConsultado.setNome(estado.getNome());
		
		return repository.save(estadoConsultado);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e){
			throw new EstadoNaoEncontradaException(id);
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Estado de código %d não pode ser removida", id));
		}
	}
	
}
