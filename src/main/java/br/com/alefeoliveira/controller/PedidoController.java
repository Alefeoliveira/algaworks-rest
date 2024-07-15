package br.com.alefeoliveira.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import br.com.alefeoliveira.api.model.PedidoInput;
import br.com.alefeoliveira.api.model.PedidoModel;
import br.com.alefeoliveira.api.model.PedidoResumoModel;
import br.com.alefeoliveira.api.util.PedidoInputDisassembler;
import br.com.alefeoliveira.api.util.PedidoModelAssembler;
import br.com.alefeoliveira.api.util.PedidoResumoModelAssembler;
import br.com.alefeoliveira.core.data.PageableTranslator;
import br.com.alefeoliveira.domain.exception.EntidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.NegocioException;
import br.com.alefeoliveira.domain.model.Pedido;
import br.com.alefeoliveira.domain.model.Usuario;
import br.com.alefeoliveira.domain.repository.PedidoRepository;
import br.com.alefeoliveira.domain.repository.filter.PedidoFilter;
import br.com.alefeoliveira.domain.service.PedidoService;
import br.com.alefeoliveira.infrastructure.repository.spec.PedidosSpecs;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private PedidoService emissaoPedido;
    
    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;
    
    @Autowired
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;
    
    @Autowired
    private PedidoInputDisassembler pedidoInputDisassembler;
    
    @GetMapping
    public Page<PedidoResumoModel> pesquisar(PedidoFilter filtro, @PageableDefault(size = 10) Pageable pageable) {
    	pageable = traduzirPageable(pageable);
    	
        Page<Pedido> pedidosPage = pedidoRepository.findAll(PedidosSpecs.usandoFiltro(filtro), pageable);
        List<PedidoResumoModel> pedidosDTO = pedidoResumoModelAssembler.toCollectionModel(pedidosPage.getContent());
        
        return new PageImpl<>(pedidosDTO, pageable, pedidosPage.getTotalElements());
    }
    
    
	/*
	 * @GetMapping public MappingJacksonValue listar(@RequestParam(required = false)
	 * String campos) { List<Pedido> todosPedidos = pedidoRepository.findAll();
	 * List<PedidoResumoModel> pedidosModel =
	 * pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
	 * 
	 * MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosModel);
	 * 
	 * SimpleFilterProvider filterProvider = new SimpleFilterProvider();
	 * filterProvider.addFilter("pedidoFilter",
	 * SimpleBeanPropertyFilter.serializeAll());
	 * 
	 * if(StringUtils.isNotBlank(campos)) { filterProvider.addFilter("pedidoFilter",
	 * SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(","))); }
	 * 
	 * pedidosWrapper.setFilters(filterProvider);
	 * 
	 * return pedidosWrapper; }
	 */
	/*
	 * @GetMapping public List<PedidoResumoModel> listar() { List<Pedido>
	 * todosPedidos = pedidoRepository.findAll();
	 * 
	 * return pedidoResumoModelAssembler.toCollectionModel(todosPedidos); }
	 */
    
    @GetMapping("/{codigoPedido}")
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);
        
        return pedidoModelAssembler.toModel(pedido);
    }  
    
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = emissaoPedido.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }
    
    private Pageable traduzirPageable(Pageable apiPageable) {
    	var mapeamento = Map.of(
    			"codigo", "codigo",
    			"restaurante.nome", "restaurante.nome",
    			"nomeCliente", "cliente.nome",
    			"valorTotal", "valorTotal"
    			);
    	
    	return PageableTranslator.translate(apiPageable, mapeamento);
    }
} 
