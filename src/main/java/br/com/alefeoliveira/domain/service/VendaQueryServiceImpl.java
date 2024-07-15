package br.com.alefeoliveira.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.alefeoliveira.domain.enumerador.StatusPedido;
import br.com.alefeoliveira.domain.model.Pedido;
import br.com.alefeoliveira.domain.model.dto.VendaDiaria;
import br.com.alefeoliveira.domain.repository.filter.VendaDiariaFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;

@Repository
public class VendaQueryServiceImpl implements VendaQueryService {
	
	@Autowired
	private EntityManager manager;
	
	@Override
	public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {
		var builder = manager.getCriteriaBuilder();
		var query = builder.createQuery(VendaDiaria.class);
		var root = query.from(Pedido.class);
		var predicates = new ArrayList<Predicate>();
		
		//var functionConvertTzDataCriacao = builder.function("convert_tz", Date.class, builder.literal("+00:00"), 
		//		builder.literal("-03:00"));
		
		//var functionDateDataCriacao = builder.function("date", Date.class, functionConvertTzDataCriacao);
		
		var functionDateDataCriacao = builder.function("date", Date.class, root.get("dataCriacao"));
		
		var selection = builder.construct(VendaDiaria.class, 
				functionDateDataCriacao, 
				builder.count(root.get("id")),
				builder.sum(root.get("valorTotal")));
		
		query.select(selection);
		
		if(filtro.getRestauranteId() != null) {
		    predicates.add(builder.equal(root.get("restaurante"), filtro.getRestauranteId()));
		}
		    
		if (filtro.getDataCriacaoInicio() != null) {
		    predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), 
		            filtro.getDataCriacaoInicio()));
		}

		if (filtro.getDataCriacaoFim() != null) {
		    predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), 
		            filtro.getDataCriacaoFim()));
		}
		
		predicates.add(root.get("status").in(
		        StatusPedido.CONFIRMADO, StatusPedido.ENTREGUE));
		
		query.where(predicates.toArray(new Predicate[0]));
		
		query.groupBy(functionDateDataCriacao);
		
		return manager.createQuery(query).getResultList();
	}

}
