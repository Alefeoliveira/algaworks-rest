package br.com.alefeoliveira.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException {
private static final long serialVersionUID = 1L;
	
	public UsuarioNaoEncontradoException(String msg) {
		super(msg);
	}
	
	public UsuarioNaoEncontradoException(Long estadoId) {
		this(String.format("Estado de código %d não pode ser encontrada", estadoId));
	}
}
