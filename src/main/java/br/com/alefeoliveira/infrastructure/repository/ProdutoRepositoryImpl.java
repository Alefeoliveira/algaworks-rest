package br.com.alefeoliveira.infrastructure.repository;

import org.springframework.stereotype.Repository;

import br.com.alefeoliveira.domain.model.FotoProduto;
import br.com.alefeoliveira.domain.repository.ProdutoRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public FotoProduto save(FotoProduto foto) {
		return entityManager.merge(foto);
	}

	@Transactional
	@Override
	public void delete(FotoProduto foto) {
		entityManager.remove(foto);
	}
}
