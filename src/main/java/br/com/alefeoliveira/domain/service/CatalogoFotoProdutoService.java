package br.com.alefeoliveira.domain.service;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alefeoliveira.domain.exception.FotoProdutoNaoEncontradaException;
import br.com.alefeoliveira.domain.model.FotoProduto;
import br.com.alefeoliveira.domain.repository.ProdutoRepository;
import br.com.alefeoliveira.domain.service.FotoStorageService.NovaFoto;
import jakarta.transaction.Transactional;

@Service
public class CatalogoFotoProdutoService {
	
	@Autowired
	private ProdutoRepository repository;
	
	@Autowired
	private FotoStorageService fotoStorageService;


	@Transactional
	public FotoProduto salvar(FotoProduto foto, InputStream inputStream) {
		//excluir foto se exister
		Long restauranteId = foto.getRestauranteId();
		Long produtoId = foto.getProduto().getId();
		String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
		String nomeArquivoExistente = null;
		
		Optional<FotoProduto> fotoExistente = repository.findFotoById(restauranteId, produtoId);
		if(fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			repository.delete(fotoExistente.get());
		}
		
		foto.setNomeArquivo(nomeNovoArquivo);
		foto = repository.save(foto);
		repository.flush();
		
		NovaFoto fotoNova = NovaFoto.builder()
				.nomeArquivo(foto.getNomeArquivo())
				.inputStream(inputStream)
				.build();
		
		fotoStorageService.substituir(nomeArquivoExistente, fotoNova);
		
		return foto;
	}
	
	public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
	    return repository.findFotoById(restauranteId, produtoId)
	            .orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
	}   
	
	@Transactional
	public void excluir(Long restauranteId, Long produtoId) {
	    FotoProduto foto = buscarOuFalhar(restauranteId, produtoId);
	    
	    repository.delete(foto);
	    repository.flush();

	    fotoStorageService.remover(foto.getNomeArquivo());
	}
}
