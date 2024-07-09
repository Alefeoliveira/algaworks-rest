package br.com.alefeoliveira.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class EstadoNaoEncontradaException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public EstadoNaoEncontradaException(String msg) {
		super(msg);
	}
	
	public EstadoNaoEncontradaException(Long estadoId) {
		this(String.format("Estado de código %d não pode ser encontrada", estadoId));
	}
}
