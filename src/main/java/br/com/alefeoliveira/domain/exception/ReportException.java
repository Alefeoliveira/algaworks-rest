package br.com.alefeoliveira.domain.exception;

public class ReportException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ReportException(String msg) {
		super(msg);
	}
}
