package br.com.alefeoliveira.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alefeoliveira.domain.model.Restaurante;

@Repository
public interface RestauranteRepository extends CustomJpaRepository<Restaurante, Long>, RestauranteRepositoryQueries,
	JpaSpecificationExecutor<Restaurante>{
	
	@Query("from Restaurante r join fetch r.cozinha left join fetch r.formasPagamento")
	List<Restaurante> findAll();
	
	List<Restaurante> queryByTaxaFreteBetween(BigDecimal tx1, BigDecimal tx2);

	//@Query("from Restaurante where nome like %:nome% and cozinha.id = :id")
	//List<Restaurante> consultarPorNome(String nome, @Param("id") Long cozinha);
	
	//List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long cozinha);
	
	//Flag first entre o find, query e entre outros e também o By, trazendo o primeiro resultado
	Optional<Restaurante> findFirstByNomeContaining(String nome);
	
	//primeiros 2 restaurantes 
	List<Restaurante> findTop2ByNomeContaining(String nome);
	
	Integer countByCozinhaId(Long cozinhaId);
}
