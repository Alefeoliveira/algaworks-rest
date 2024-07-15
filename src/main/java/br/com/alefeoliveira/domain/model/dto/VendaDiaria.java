package br.com.alefeoliveira.domain.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class VendaDiaria {
	
	private Date data;
	private Long totalVendas;
	private BigDecimal totalFaturado;
}
