package br.com.alefeoliveira.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.alefeoliveira.domain.exception.CidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.EntidadeEmUsoException;
import br.com.alefeoliveira.domain.exception.EntidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.model.Cidade;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.model.Estado;
import br.com.alefeoliveira.domain.model.Restaurante;
import br.com.alefeoliveira.domain.repository.CidadeRepository;
import br.com.alefeoliveira.domain.repository.CozinhaRepository;
import jakarta.transaction.Transactional;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repository;
	
	@Autowired
	private EstadoService estadoService;

	@Transactional
	public Cidade salvar(Cidade cidade) {
		return repository.save(cidade);
	}
	
	public Cidade buscar(Long id) {
		return repository.findById(id).orElseThrow(() -> new CidadeNaoEncontradaException(id));
	}
	
	public List<Cidade> buscarTodos() {
		return repository.findAll();
	}
	
	@Transactional
	public Cidade atualizar(Cidade cidade, Long id) {
		Cidade cidadeConsultado = buscar(id);
		
		Estado estado = estadoService.buscar(cidade.getEstado().getId());
		
		cidadeConsultado.setEstado(estado);
		
		return this.salvar(cidadeConsultado);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e){
			throw new CidadeNaoEncontradaException(id);
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Cidade de código %d não pode ser removida", id));
		}
	}
	
}
