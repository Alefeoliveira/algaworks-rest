package br.com.alefeoliveira.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import br.com.alefeoliveira.Groups;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

//@JsonRootName("cozinha")
@Entity
@Data
@Relation(collectionRelation = "cozinhas")
public class Cozinha extends RepresentationModel<Cozinha> {
	
	//@NotNull(groups = Groups.CozinhaId.class)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	

	@NotBlank
	@Column(nullable = false)
	//@JsonProperty("titulo")
	//@JsonIgnore
	private String nome;
	
	@JsonIgnore
	@OneToMany(mappedBy = "cozinha")
	private List<Restaurante> restaurantes = new ArrayList<>();
}
