package br.com.alefeoliveira.domain.model;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class RestauranteMixin {

		@JsonIgnoreProperties(value = "nome")
		private Cozinha cozinha;
		
		@JsonIgnore
		private List<Produto> produtos = new ArrayList<>();
		
		@JsonIgnore
		private Endereco endereco;
		
		@JsonIgnore
		private OffsetDateTime dataCadastro;
		
		@JsonIgnore
		private OffsetDateTime dataAtualizacao;
		
		@JsonIgnore
		private List<FormaPagamento> formasPagamento = new ArrayList<>();
}
