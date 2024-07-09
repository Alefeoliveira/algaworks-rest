package br.com.alefeoliveira.jpa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alefeoliveira.domain.model.Restaurante;
import br.com.alefeoliveira.domain.repository.RestauranteRepository;
import br.com.alefeoliveira.infrastructure.repository.spec.RestauranteSpecs;

@RestController
@RequestMapping("teste")
public class TestController {
	
	@Autowired
	private RestauranteRepository repo;
	
	
	@GetMapping("/por-nome-e-frete")
	public List<Restaurante> buscarPorNomeFrete(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
		List<Restaurante> res = repo.find(nome, taxaFreteInicial, taxaFreteFinal);
		return res;
	}
	
	@GetMapping("/com-frete-gratis")
	public List<Restaurante> restaurantesComFreteGratis(String nome){
		return repo.findComFreteGratis(nome);
	}
	
	@GetMapping("/busca-primeiro")
	public Optional<Restaurante> restaurantesBuscaPrimeiro(){
		return repo.buscarPrimeiro();
	}
}
