package br.com.alefeoliveira.domain.exception;

public class FormaPagamentoNaoEncontradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	 public FormaPagamentoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
    
    public FormaPagamentoNaoEncontradaException(Long formaPagamentoId) {
        this(String.format("Não existe um cadastro de forma de pagamento com código %d", formaPagamentoId));
    }
}
