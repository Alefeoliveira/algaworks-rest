package br.com.alefeoliveira;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import br.com.alefeoliveira.domain.model.ValorZeroIncluiDescricao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;

public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {
	
	private String valorField;
	private String descricaoField;
	private String descricaoObrigatoria;

	@Override
	public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
		this.valorField = constraintAnnotation.valorField();
		this.descricaoField = constraintAnnotation.descricaoField();
		this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
	}
	
	@Override
	public boolean isValid(Object objValidacao, ConstraintValidatorContext context) {
		boolean valido = true;
		if(objValidacao != null) {
			try {
				BigDecimal valor = (BigDecimal) BeanUtils.getPropertyDescriptor(objValidacao.getClass(), valorField)
						.getReadMethod().invoke(objValidacao);
				
				String descricao = (String) BeanUtils.getPropertyDescriptor(objValidacao.getClass(), descricaoField)
						.getReadMethod().invoke(objValidacao);
				
				if(valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
					valido = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
				}
			} catch (Exception e) {
				throw new ValidationException();
			} 
		}
		return valido;
	}

}
