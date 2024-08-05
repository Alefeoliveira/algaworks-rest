package br.com.alefeoliveira.domain.service;

import java.io.InputStream;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import br.com.alefeoliveira.core.storage.StorageProperties;
import br.com.alefeoliveira.domain.exception.StorageException;

public class S3FotoStorageService implements FotoStorageService {
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private StorageProperties properties;

	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		String caminhoArquivo = getCaminhoArquivo(nomeArquivo);
		
		URL url = amazonS3.getUrl(properties.getS3().getBucket(), caminhoArquivo);
		
		return FotoRecuperada.builder()
				.url(url.toString())
				.build();
	}

	@Override
	public void armazenar(NovaFoto novaFoto) {
		try {
			String caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());
			
			var objMetaData = new ObjectMetadata();
			objMetaData.setContentType(novaFoto.getContentType());
			
			var putObjectRequest = new PutObjectRequest(properties.getS3()
					.getBucket(), caminhoArquivo, novaFoto.getInputStream(), objMetaData)
					.withCannedAcl(CannedAccessControlList.PublicRead);
			
			amazonS3.putObject(putObjectRequest);
		}catch(Exception e) {
			throw new StorageException("Não foi possivel enviar arquivo para amazon S3.");
		}
	}

	private String getCaminhoArquivo(String nomeArquivo) {
		return String.format("%s/%s", properties.getS3().getDiretorioFotos(), nomeArquivo);
	}

	@Override
	public void remover(String nomeArquivo) {
		try {
	        String caminhoArquivo = getCaminhoArquivo(nomeArquivo);

	        var deleteObjectRequest = new DeleteObjectRequest(
	                properties.getS3().getBucket(), caminhoArquivo);

	        amazonS3.deleteObject(deleteObjectRequest);
	    } catch (Exception e) {
	        throw new StorageException("Não foi possível excluir arquivo na Amazon S3.", e);
	    }
	}

}
