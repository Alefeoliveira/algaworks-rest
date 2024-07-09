package br.com.alefeoliveira.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.alefeoliveira.domain.exception.CozinhaNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.EntidadeEmUsoException;
import br.com.alefeoliveira.domain.exception.EntidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.repository.CozinhaRepository;
import jakarta.transaction.Transactional;

@Service
public class CozinhaService {
	
	@Autowired
	private CozinhaRepository repository;

	@Transactional
	public Cozinha salvar(Cozinha cozinha) {
		return repository.save(cozinha);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.deleteById(id);
			repository.flush();
		}catch(EmptyResultDataAccessException e){
			throw new CozinhaNaoEncontradaException(id);
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Cozinha de código %d não pode ser removida", id));
		}
	}
	
	public Cozinha buscarOuFalhar(Long cozinhaId) {
		return repository.findById(cozinhaId).orElseThrow(()-> new CozinhaNaoEncontradaException(cozinhaId));
	}
	
}
