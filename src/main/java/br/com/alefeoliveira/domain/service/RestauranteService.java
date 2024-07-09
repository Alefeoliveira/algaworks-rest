package br.com.alefeoliveira.domain.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.alefeoliveira.domain.exception.EntidadeEmUsoException;
import br.com.alefeoliveira.domain.exception.EntidadeNaoEncontradaException;
import br.com.alefeoliveira.domain.exception.RestauranteNaoEncontradaException;
import br.com.alefeoliveira.domain.model.Cidade;
import br.com.alefeoliveira.domain.model.Cozinha;
import br.com.alefeoliveira.domain.model.FormaPagamento;
import br.com.alefeoliveira.domain.model.Restaurante;
import br.com.alefeoliveira.domain.model.Usuario;
import br.com.alefeoliveira.domain.repository.CidadeRepository;
import br.com.alefeoliveira.domain.repository.CozinhaRepository;
import br.com.alefeoliveira.domain.repository.FormaPagamentoRepository;
import br.com.alefeoliveira.domain.repository.RestauranteRepository;
import jakarta.transaction.Transactional;

@Service
public class RestauranteService {
	
	@Autowired
	private RestauranteRepository repository;
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private CozinhaService cozinhaService;
	
	@Autowired
	private FormaPagamentoService formaPagamentoService;
	
	@Autowired
	private UsuarioService cadastroUsuario;
	
	@Autowired
	private CidadeService cidadeService;
	
	public Restaurante buscar(Long id) {
		return repository.findById(id).orElseThrow(() -> new RestauranteNaoEncontradaException(id));
	}
	
	public List<Restaurante> buscarTodos() {
		return repository.findAll();
	}

	@Transactional
	public Restaurante salvar(Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Long cidadeId = restaurante.getEndereco().getCidade().getId();
		
		Cozinha cozinha = cozinhaService.buscarOuFalhar(cozinhaId);
		Cidade cidade = cidadeService.buscar(cidadeId);

		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		return repository.save(restaurante);
	}
	
	@Transactional
	public Restaurante atualizar(Restaurante restaurante, Long id) {
		Restaurante restaruanteConsultado = buscar(id);
		
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaService.buscarOuFalhar(cozinhaId);
		
		restaruanteConsultado.setNome(restaurante.getNome());
		restaruanteConsultado.setTaxaFrete(restaurante.getTaxaFrete());
		cozinha.setNome(restaurante.getCozinha().getNome());
		restaruanteConsultado.setCozinha(cozinha);
	
		return repository.save(restaurante);
	}
	
	@Transactional
	public void ativar(Long restauranteID) {
		Restaurante res = buscar(restauranteID);
		
		res.ativar();
	}
	
	@Transactional
	public void ativar(List<Long> restauranteID) {
		restauranteID.forEach(this::ativar);
	}
	
	@Transactional
	public void inativar(Long restauranteID) {
		Restaurante res = buscar(restauranteID);
		
		res.inativar();
	}
	
	@Transactional
	public void inativar(List<Long> restauranteID) {
		restauranteID.forEach(this::inativar);
	}
	
	@Transactional
	public void removerFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante res = buscar(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoService.buscarOuFalhar(formaPagamentoId);
		res.removerFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void adicionarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante res = buscar(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoService.buscarOuFalhar(formaPagamentoId);
		res.adicionarFormaPagamento(formaPagamento);
	}
	
	@Transactional
	public void abrir(Long restauranteId) {
	    Restaurante restauranteAtual = buscar(restauranteId);
	    
	    restauranteAtual.abrir();
	}
	
	@Transactional
	public void fechar(Long restauranteId) {
	    Restaurante restauranteAtual = buscar(restauranteId);
	    
	    restauranteAtual.fechar();
	}   
	
	@Transactional
	public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
	    Restaurante restaurante = buscar(restauranteId);
	    Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
	    
	    restaurante.removerResponsavel(usuario);
	}

	@Transactional
	public void associarResponsavel(Long restauranteId, Long usuarioId) {
	    Restaurante restaurante = buscar(restauranteId);
	    Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);
	    
	    restaurante.adicionarResponsavel(usuario);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e){
			throw new RestauranteNaoEncontradaException(id);
		}catch(DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Cozinha de código %d não pode ser removida", id));
		}
	}
	
}
