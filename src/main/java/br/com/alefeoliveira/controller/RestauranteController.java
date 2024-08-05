package br.com.alefeoliveira.controller;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alefeoliveira.Groups;
import br.com.alefeoliveira.api.model.CozinhaDTO;
import br.com.alefeoliveira.api.model.RestauranteDTO;
import br.com.alefeoliveira.api.util.RestauranteUtil;
import br.com.alefeoliveira.domain.exception.EntidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.NegocioException;
import br.com.alefeoliveira.domain.exception.RestauranteNaoEncontradaException;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.model.Restaurante;
import br.com.alefeoliveira.domain.model.view.RestauranteView;
import br.com.alefeoliveira.domain.service.RestauranteService;
import br.com.alefeoliveira.exceptionhandler.ValidacaoExcpetion;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;

@RestController
@RequestMapping(value = "/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController {
	
	@Autowired
	private RestauranteService service;
	
	@Autowired
	private RestauranteUtil util;
	
	@Autowired
	private SmartValidator validator;
	
	
	@GetMapping
	@JsonView(RestauranteView.Resumo.class) 
	public List<RestauranteDTO> listar() {
		List<Restaurante> restaurantes = service.buscarTodos(); 
		return util.toCollectionDTO(restaurantes); 
	}
		
	 @GetMapping(params = "projecao=apenas-nome")
	 @JsonView(RestauranteView.ApenasNome.class) 
	 public List<RestauranteDTO> listarRestaurantesApenasNome() { 
		 return listar(); 
	 }
	
	/*
	 * @GetMapping public MappingJacksonValue
	 * listarRestaurantes(@RequestParam(required = false) String projecao) {
	 * List<Restaurante> restaurantes = service.buscarTodos(); List<RestauranteDTO>
	 * pedidosModel = util.toCollectionDTO(restaurantes); MappingJacksonValue
	 * pedidosWrapper = new MappingJacksonValue(pedidosModel);
	 * 
	 * pedidosWrapper.setSerializationView(RestauranteView.Resumo.class);
	 * 
	 * if("apenas-nome".equals(projecao)) {
	 * pedidosWrapper.setSerializationView(RestauranteView.ApenasNome.class); } else
	 * if ("completo".equals(projecao)) { pedidosWrapper.setSerializationView(null);
	 * }
	 * 
	 * 
	 * return pedidosWrapper; }
	 */
	
	/*
	 * @GetMapping public List<RestauranteDTO> listarRestaurantes() {
	 * List<Restaurante> restaurantes = service.buscarTodos(); return
	 * util.toCollectionDTO(restaurantes); }
	 * 
	 * @GetMapping("/resumo")
	 * 
	 * @JsonView(RestauranteView.Resumo.class) public List<RestauranteDTO>
	 * listarRestaurantesResumo() { return listarRestaurantes(); }
	 */
	
	
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public RestauranteDTO salvar(@RequestBody  @Valid RestauranteDTO restauranteDTO){
		try {
			Restaurante restaurante = util.toRestaurante(restauranteDTO);
			return util.toDTO(service.salvar(restaurante));
		}catch(EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}
	
	@PutMapping("/{restauranteId}")
	public RestauranteDTO atualizar(@PathVariable("restauranteId") Long id, @RequestBody @Valid RestauranteDTO restauranteDTO){
		//Restaurante restaurante = util.toRestaurante(restauranteDTO);
		
		Restaurante restauranteAtual = service.buscar(id);
		
		util.copyToDomainObject(restauranteDTO, restauranteAtual);
	    
	   // BeanUtils.copyProperties(restaurante, restauranteAtual, 
	    //        "id", "formasPagamento", "endereco", "dataCadastro", "produtos");
	    
	    try {
	        return util.toDTO(service.salvar(restauranteAtual));
	    } catch (EntidadeNaoEncontradaException  e) {
	        throw new NegocioException(e.getMessage());
	    }
	}

	@GetMapping("/{restauranteId}")
	public RestauranteDTO buscar(@PathVariable("restauranteId") Long id) {
		Restaurante restaurante = service.buscar(id);
		
		return util.toDTO(restaurante);
	}

	
	
	@PatchMapping("/{restauranteId}")
	public RestauranteDTO atualizarParcial(@PathVariable("restauranteId") Long id, 
			@RequestBody Map<String, Object> campos, HttpServletRequest request) {
		Restaurante resAtual = service.buscar(id);
		
		merge(campos, resAtual, request);
		validate(resAtual, "restaurante");
		RestauranteDTO restauranteDTO = util.toDTO(resAtual);
		
		return atualizar(id, restauranteDTO);
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long id) {
		service.ativar(id);
	}
	
	@DeleteMapping("/{id}/ativo")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long id) {
		service.inativar(id);
	}
	
	
	private void validate(Restaurante resAtual, String objectName) {
		BeanPropertyBindingResult bidingResult = new BeanPropertyBindingResult(resAtual, objectName);
		validator.validate(resAtual, bidingResult);
		
		if(bidingResult.hasErrors()) {
			throw new ValidacaoExcpetion(bidingResult);
		}
	}

	private void merge(Map<String, Object> campos, Restaurante resAtual, HttpServletRequest request) {
		ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Restaurante origem = objectMapper.convertValue(campos, Restaurante.class);
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
			
			campos.forEach((nomePropriedade, valorPropriedade) -> {
				 Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
				 field.setAccessible(true);
				 
				 Object novoValor = ReflectionUtils.getField(field, origem);
				 
				 ReflectionUtils.setField(field, resAtual, novoValor);
			});
		} catch(IllegalArgumentException e) {
			Throwable rootCause = ExceptionUtils.getRootCause(e);
			throw new HttpMessageNotReadableException(e.getMessage(), rootCause, serverHttpRequest);
		}
	}
	
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void abrir(@PathVariable Long restauranteId) {
	    service.abrir(restauranteId);
	}

	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void fechar(@PathVariable Long restauranteId) {
		service.fechar(restauranteId);
	} 
	
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restaurantesId) {
		try {
			service.ativar(restaurantesId);
		}catch(RestauranteNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	

	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restaurantesId) {
		try {
			service.inativar(restaurantesId);
		}catch(RestauranteNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
}
