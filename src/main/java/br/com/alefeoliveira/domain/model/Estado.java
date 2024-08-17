package br.com.alefeoliveira.domain.model;

import org.springframework.hateoas.RepresentationModel;

import br.com.alefeoliveira.Groups;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Estado extends RepresentationModel<Estado>{
	
	@NotNull(groups = Groups.EstadoId.class)
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@NotBlank
	private String nome;
}
