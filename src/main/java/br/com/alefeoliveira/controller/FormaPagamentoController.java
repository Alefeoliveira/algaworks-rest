package br.com.alefeoliveira.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import br.com.alefeoliveira.api.model.FormaPagamentoDTO;
import br.com.alefeoliveira.api.util.FormaPagamentoUtil;
import br.com.alefeoliveira.domain.model.FormaPagamento;
import br.com.alefeoliveira.domain.repository.FormaPagamentoRepository;
import br.com.alefeoliveira.domain.service.FormaPagamentoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/formas-pagamento")
public class FormaPagamentoController {

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;
    
    @Autowired
    private FormaPagamentoService cadastroFormaPagamento;
    
    @Autowired
    private FormaPagamentoUtil formaPagamentoUtil;

    @GetMapping
    public ResponseEntity<List<FormaPagamentoDTO>> listar(ServletWebRequest request) {
    	ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
    	
    	String eTag = "0";
    	
    	OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();
    	
    	if(dataUltimaAtualizacao != null) {
    		eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
    	}
    	
    	if(request.checkNotModified(eTag)) {
    		return null;
    	}
    	
        List<FormaPagamento> todasFormasPagamentos = formaPagamentoRepository.findAll();
        
        List<FormaPagamentoDTO> formasPagamentoModel = formaPagamentoUtil.toCollectionDTO(todasFormasPagamentos);
        
        return ResponseEntity.ok()
        		.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
        		.eTag(eTag)
        		.body(formasPagamentoModel);
    }
    
    @GetMapping("/{formaPagamentoId}")
    public ResponseEntity<FormaPagamentoDTO> buscar(@PathVariable Long formaPagamentoId) {
      FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
      
      FormaPagamentoDTO formaPagamentoModel =  formaPagamentoUtil.toDTO(formaPagamento);
      
      return ResponseEntity.ok()
          .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
          .body(formaPagamentoModel);
    }
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public FormaPagamentoDTO adicionar(@RequestBody @Valid FormaPagamentoDTO formaPagamentoInput) {
        FormaPagamento formaPagamento = formaPagamentoUtil.toFormaPagamento(formaPagamentoInput);
        
        formaPagamento = cadastroFormaPagamento.salvar(formaPagamento);
        
        return formaPagamentoUtil.toDTO(formaPagamento);
    }
    
    @PutMapping("/{formaPagamentoId}")
    public FormaPagamentoDTO atualizar(@PathVariable Long formaPagamentoId,
            @RequestBody @Valid FormaPagamentoDTO formaPagamentoInput) {
        FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);
        
        formaPagamentoUtil.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);
        
        formaPagamentoAtual = cadastroFormaPagamento.salvar(formaPagamentoAtual);
        
        return formaPagamentoUtil.toDTO(formaPagamentoAtual);
    }
    
    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long formaPagamentoId) {
        cadastroFormaPagamento.excluir(formaPagamentoId);	
    }   
}       
