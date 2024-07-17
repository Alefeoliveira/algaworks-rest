package br.com.alefeoliveira;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileTypeValidator implements ConstraintValidator<FileInputType, MultipartFile> {
	
	 private List<String> allowedContentTypes;

	@Override
	public void initialize(FileInputType constraintAnnotation) {
		this.allowedContentTypes = Arrays.asList(constraintAnnotation.allowed());
	}
	
	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		return value == null 
                || this.allowedContentTypes.contains(value.getContentType());
	}
}
