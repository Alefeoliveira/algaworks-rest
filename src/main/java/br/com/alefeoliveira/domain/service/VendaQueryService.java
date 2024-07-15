package br.com.alefeoliveira.domain.service;

import java.util.List;

import br.com.alefeoliveira.domain.model.dto.VendaDiaria;
import br.com.alefeoliveira.domain.repository.filter.VendaDiariaFilter;

public interface VendaQueryService {
	List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filter);
}
