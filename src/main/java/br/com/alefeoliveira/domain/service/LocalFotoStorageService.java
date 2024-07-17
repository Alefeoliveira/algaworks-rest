package br.com.alefeoliveira.domain.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import br.com.alefeoliveira.domain.exception.StorageException;

@Service
public class LocalFotoStorageService implements FotoStorageService {
	
	@Value("${algafood.storage.local.diretorio-fotos}")
	private Path diretorio;

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possivel armazenar arquivo", e);
		}
	}
	
	private Path getArquivoPath(String nomeArquivo) {
		return diretorio.resolve(Path.of(nomeArquivo));
	}

	@Override
	public void remover(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possivel excluir arquivo", e);
		}
	}

	@Override
	public InputStream recuperar(String nomeArquivo) {
		try {
			Path arquivoPath = getArquivoPath(nomeArquivo);
			return Files.newInputStream(arquivoPath);
		} catch (IOException e) {
			throw new StorageException("Não foi possivel recuperar o arquivo", e);
		}
	}
}
