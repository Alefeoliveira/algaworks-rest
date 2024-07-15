package br.com.alefeoliveira.exceptionhandler;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

import br.com.alefeoliveira.domain.enumerador.ProblemType;
import br.com.alefeoliveira.domain.exception.EntidadeEmUsoException;
import br.com.alefeoliveira.domain.exception.EntidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	private static final String MSG_ERRO_GENERICA_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. "
            + "Tente novamente e se o problema persistir, entre em contato "
            + "com o administrador do sistema.";
	
	@Autowired
	private MessageSource msgSource;
	
	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatusCode status,
			WebRequest request) {
		
		return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		 if (rootCause instanceof InvalidFormatException) {
		        return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
		    } else if (rootCause instanceof PropertyBindingException) {
		        return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request); 
		    }
		
		HttpStatus statusHttp = HttpStatus.valueOf(status.value());
		Problema problema = createProblemBuilder(statusHttp, ProblemType.MENSAGEM_INCOMPREENSIVEL, 
				"O corpo da requisição está inválido. Verifique erro da sintaxe.").build();
	
		return this.handleExceptionInternal(ex, problema, 
				new HttpHeaders(), status, request);

	}
	
	@Override
	protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		 ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		 String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", 
		            ex.getResourcePath());
		    
		 Problema problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, detail).build();
		    
		 return handleExceptionInternal(ex, problem, headers, status, request);
	}

	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, 
	        HttpHeaders headers, HttpStatusCode status, WebRequest request) {
	    
	    ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
	    String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", 
	            ex.getRequestURL());
	    
	    Problema problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, detail).build();
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	}  

	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
	    
	    if (ex instanceof MethodArgumentTypeMismatchException) {
	        return handleMethodArgumentTypeMismatch(
	                (MethodArgumentTypeMismatchException) ex, headers, status, request);
	    }

	    return super.handleTypeMismatch(ex, headers, status, request);
	}

	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
	        MethodArgumentTypeMismatchException ex, HttpHeaders headers,
	        HttpStatusCode status, WebRequest request) {

	    ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

	    String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
	            + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
	            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

	    Problema problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, detail).build();

	    return handleExceptionInternal(ex, problem, headers, status, request);
	}

	
	private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex,
	        HttpHeaders headers, HttpStatusCode status, WebRequest request) {

	  
	    String path = joinPath(ex.getPath());
	    
	    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
	    String detail = String.format("A propriedade '%s' não existe. "
	            + "Corrija ou remova essa propriedade e tente novamente.", path);
	    
	    String userMessage = "Ocorreu um erro interno inesperado no sistema. "
	            + "Tente novamente e se o problema persistir, entre em contato "
	            + "com o administrador do sistema.";


	    Problema problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, detail)
	    		.userMessage(userMessage)
	    		.build();
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	} 
	
	private String joinPath(List<Reference> references) {
	    return references.stream()
	        .map(ref -> ref.getFieldName())
	        .collect(Collectors.joining("."));
	}     

	protected ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		String path = joinPath(ex.getPath());
	
		String detail = String.format("A propriedade '%s' recevbeu o valor '%s', que é de um tipo inválido."
				+ "Corrija e informe um valor compatível com o tipo %s.", path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		String userMessage = "Ocorreu um erro interno inesperado no sistema. "
	            + "Tente novamente e se o problema persistir, entre em contato "
	            + "com o administrador do sistema.";


	    Problema problema = createProblemBuilder(HttpStatus.valueOf(status.value()), ProblemType.MENSAGEM_INCOMPREENSIVEL, detail)
	    		.userMessage(userMessage)
	    		.build();
	    
		return this.handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<?> handleEntidadeNaoEncontradoException(
			EntidadeNaoEncontradaException e, WebRequest request){
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		Problema problema = createProblemBuilder(status, ProblemType.RECURSO_NAO_ENCONTRADO, e.getMessage()).build();
	
		return this.handleExceptionInternal(e, problema, 
				new HttpHeaders(), HttpStatusCode.valueOf(status.value()), request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<?> handleNegocioException(
			NegocioException e,  WebRequest request){
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Problema problema = createProblemBuilder(status, ProblemType.NEGOCIO_EXCEPTION, e.getMessage()).build();
		
		return this.handleExceptionInternal(e, e.getMessage(), 
				new HttpHeaders(), HttpStatusCode.valueOf(status.value()), request);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<?> handleEntidadeEmUsoException(
			EntidadeEmUsoException e, WebRequest request){
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Problema problema = createProblemBuilder(status, ProblemType.ENTIDADE_EM_USO, e.getMessage())
				.userMessage(e.getMessage()).build();
		
		
		return this.handleExceptionInternal(e, e.getMessage(), 
				new HttpHeaders(), HttpStatusCode.valueOf(status.value()), request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		if(body == null) {
			body = Problema.builder()
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.title(HttpStatus.valueOf(statusCode.value()).getReasonPhrase())
					.status(statusCode.value()).build();
		} else if (body instanceof String) {
			body = Problema.builder()
					.userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
					.timestamp(OffsetDateTime.now())
					.title((String) body)
					.status(statusCode.value()).build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, statusCode, request);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
	    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
	    ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
	    String detail = "Ocorreu um erro interno inesperado no sistema. "
	            + "Tente novamente e se o problema persistir, entre em contato "
	            + "com o administrador do sistema.";

	    // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
	    // fazendo logging) para mostrar a stacktrace no console
	    // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
	    // para você durante, especialmente na fase de desenvolvimento
	    ex.printStackTrace();
	    
	    Problema problem = createProblemBuilder(status, problemType, detail).build();

	    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}  
	
	private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		        
		    ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		    String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
		    
		    List<Problema.Object> problemObjects = bindingResult.getAllErrors().stream()
		            .map(objectError -> {
		                String message = msgSource.getMessage(objectError, LocaleContextHolder.getLocale());
		                
		                String name = objectError.getObjectName();
		                
		                if (objectError instanceof FieldError) {
		                    name = ((FieldError) objectError).getField();
		                }
		                
		                return Problema.Object.builder()
		                    .name(name)
		                    .userMessage(message)
		                    .build();
		            })
		            .collect(Collectors.toList());
		    
		    Problema problem = createProblemBuilder(HttpStatus.valueOf(status.value()), problemType, detail)
		        .userMessage(detail)
		        .objects(problemObjects)
		        .build();
		    
		    return handleExceptionInternal(ex, problem, headers, status, request);
		}
	
	@ExceptionHandler({ ValidacaoExcpetion.class })
	public ResponseEntity<Object> handleValidacaoException(ValidacaoExcpetion ex, WebRequest request) {
	    return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), 
	            HttpStatus.BAD_REQUEST, request);
	}             

	
	
	private Problema.ProblemaBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
		return Problema.builder()
				.timestamp(OffsetDateTime.now())
				.status(status.value())
				.type(problemType.getUri())
				.title(problemType.getTitle())
				.detail(detail);		
	}
}
