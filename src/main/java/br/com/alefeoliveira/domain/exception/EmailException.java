package br.com.alefeoliveira.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailException extends EntidadeNaoEncontradaException {

	private static final long serialVersionUID = 1L;
	
	public EmailException(String msg) {
		super(msg);
	}
}
