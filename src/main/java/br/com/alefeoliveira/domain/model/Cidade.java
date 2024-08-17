package br.com.alefeoliveira.domain.model;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import br.com.alefeoliveira.Groups;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import lombok.Data;

@Relation(collectionRelation = "cidades")
@Entity
@Data
@Schema(name = "Cidade", description = "Representa uma cidade")
public class Cidade extends RepresentationModel<Cidade>{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Schema(name = "Id da cidade", example = "1")
	private Long id;
	
	@NotBlank
	@Column(nullable = false)
	@Schema(example = "Uberlandia")
	private String nome;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estado_id", nullable = false)
	@ConvertGroup(from = Default.class, to = Groups.EstadoId.class)
	@Valid
	@NotNull
	private Estado estado;
}
