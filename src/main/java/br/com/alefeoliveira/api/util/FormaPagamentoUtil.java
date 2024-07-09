package br.com.alefeoliveira.api.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alefeoliveira.api.model.FormaPagamentoDTO;
import br.com.alefeoliveira.api.model.RestauranteDTO;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.model.FormaPagamento;
import br.com.alefeoliveira.domain.model.Restaurante;

@Component
public class FormaPagamentoUtil {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public FormaPagamentoDTO toDTO(FormaPagamento formaPagamento) {
		return modelMapper.map(formaPagamento, FormaPagamentoDTO.class);
	}
	
	public List<FormaPagamentoDTO> toSetDTO(Set<FormaPagamento> formasPagamentos){
		return formasPagamentos.stream().map(formaPagamento -> toDTO(formaPagamento)).collect(Collectors.toList());
	}
	
	public List<FormaPagamentoDTO> toCollectionDTO(List<FormaPagamento> formasPagamentos){
		return formasPagamentos.stream().map(formaPagamento -> toDTO(formaPagamento)).collect(Collectors.toList());
	}
	
	public FormaPagamento toFormaPagamento(FormaPagamentoDTO dto) {
		return modelMapper.map(dto, FormaPagamento.class);
	}
	
	public void copyToDomainObject(FormaPagamentoDTO dto, FormaPagamento forma) {
		modelMapper.map(dto, forma);
	}

}
