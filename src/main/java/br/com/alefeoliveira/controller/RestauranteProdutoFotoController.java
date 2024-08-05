package br.com.alefeoliveira.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.alefeoliveira.api.model.FotoInputProduto;
import br.com.alefeoliveira.api.model.FotoProdutoModel;
import br.com.alefeoliveira.api.model.FotoProdutoModelAssembler;
import br.com.alefeoliveira.domain.exception.EntidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.model.FotoProduto;
import br.com.alefeoliveira.domain.model.Produto;
import br.com.alefeoliveira.domain.service.CatalogoFotoProdutoService;
import br.com.alefeoliveira.domain.service.FotoStorageService;
import br.com.alefeoliveira.domain.service.FotoStorageService.FotoRecuperada;
import br.com.alefeoliveira.domain.service.ProdutoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {
	
	@Autowired
	private CatalogoFotoProdutoService service;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private FotoProdutoModelAssembler assembler;
	
	@Autowired
	private FotoStorageService fotoService;

	@PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, 
			@Valid FotoInputProduto inputFile) throws IOException {
		Produto produto = produtoService.buscarOuFalhar(restauranteId, produtoId);
		
		MultipartFile arquivo = inputFile.getArquivo();
		
		FotoProduto foto = new FotoProduto();
		foto.setProduto(produto);
		foto.setNomeArquivo(arquivo.getName());
		foto.setDescricao(inputFile.getDescricao());
		foto.setContentType(arquivo.getContentType());
		foto.setTamanho(arquivo.getSize());
		
		FotoProduto fotoSalva = service.salvar(foto, arquivo.getInputStream());
		
		return assembler.toModel(fotoSalva);
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public FotoProdutoModel buscar(@PathVariable Long restauranteId, 
	        @PathVariable Long produtoId) {
	    FotoProduto fotoProduto = service.buscarOuFalhar(restauranteId, produtoId);
	    
	    return assembler.toModel(fotoProduto);
	}

	@GetMapping
	public ResponseEntity<?> servirFoto(@PathVariable Long restauranteId, 
	        @PathVariable Long produtoId, @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
		try {
			FotoProduto fotoProduto = service.buscarOuFalhar(restauranteId, produtoId);
			List<MediaType> mediatypesAceitas = MediaType.parseMediaTypes(acceptHeader);
			
			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
			
			verificarCompatibilidadeMediaType(mediaTypeFoto, mediatypesAceitas);
			
			FotoRecuperada fotoRecuperada = fotoService.recuperar(fotoProduto.getNomeArquivo());
			
			if(fotoRecuperada.temUrl()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
						.build();
			}else {
				return ResponseEntity.ok()
						.contentType(MediaType.IMAGE_JPEG)
						.body(new InputStreamResource(fotoRecuperada.getInputStream()));
			}
		}catch(EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
		
	}
	
	@DeleteMapping
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long restauranteId, 
	        @PathVariable Long produtoId) {
		service.excluir(restauranteId, produtoId);
	}   

	private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediatypesAceitas) throws HttpMediaTypeNotAcceptableException {
		boolean compativel = mediatypesAceitas.stream().anyMatch(mediatypesAceita -> mediatypesAceita.isCompatibleWith(mediaTypeFoto));
		
		if(!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediatypesAceitas);
		}
	}
}
