package br.com.alefeoliveira.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import br.com.alefeoliveira.core.storage.StorageProperties;
import br.com.alefeoliveira.core.storage.StorageProperties.TipoStorage;
import br.com.alefeoliveira.domain.service.FotoStorageService;
import br.com.alefeoliveira.domain.service.LocalFotoStorageService;
import br.com.alefeoliveira.domain.service.S3FotoStorageService;

@Configuration
public class StorageConfig {
	
	@Autowired
	private StorageProperties properties;

	@Bean
	public AmazonS3 amazonS3() {
		var credentials = new BasicAWSCredentials(properties.getS3().getIdChaveAcesso(), properties.getS3().getChaveAcessoSecreta());
		return AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(properties.getS3().getRegiao())
				.build();
	}
	
	@Bean
	public FotoStorageService fotoStorageService() {
		if(TipoStorage.S3.equals(properties.getTipo())){
			return new S3FotoStorageService();
		}else {
			return new LocalFotoStorageService();
		}
	}
}
