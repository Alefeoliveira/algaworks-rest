package br.com.alefeoliveira.domain.service;

import br.com.alefeoliveira.domain.repository.filter.VendaDiariaFilter;

public interface VendaReportService {
	byte[] emitirVendasDiarias(VendaDiariaFilter filter);
}
