package br.com.alefeoliveira.api.model;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import br.com.alefeoliveira.FileInputType;
import br.com.alefeoliveira.FileSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoInputProduto {
	
	@NotNull
	@FileSize(max = "500KB")
	@FileInputType(allowed = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE })
	private MultipartFile arquivo;
	
	@NotBlank
	private String descricao;
}
