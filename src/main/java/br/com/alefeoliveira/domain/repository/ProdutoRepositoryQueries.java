package br.com.alefeoliveira.domain.repository;

import br.com.alefeoliveira.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

	FotoProduto save(FotoProduto foto);
	void delete(FotoProduto foto);
}
