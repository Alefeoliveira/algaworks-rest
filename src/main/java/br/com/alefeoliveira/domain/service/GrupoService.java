package br.com.alefeoliveira.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.alefeoliveira.domain.exception.EntidadeEmUsoException;
import br.com.alefeoliveira.domain.model.Grupo;
import br.com.alefeoliveira.domain.model.Permissao;
import br.com.alefeoliveira.domain.repository.GrupoRepository;
import jakarta.transaction.Transactional;

@Service
public class GrupoService {

    private static final String MSG_GRUPO_EM_USO 
        = "Grupo de código %d não pode ser removido, pois está em uso";
    
    @Autowired
    private GrupoRepository grupoRepository;
    
    @Transactional
    public Grupo salvar(Grupo grupo) {
        return grupoRepository.save(grupo);
    }
    
    @Transactional
    public void excluir(Long grupoId) {
        try {
            grupoRepository.deleteById(grupoId);
            grupoRepository.flush();
            
        } catch (EmptyResultDataAccessException e) {
        	throw new EntidadeEmUsoException(
                    String.format(MSG_GRUPO_EM_USO, grupoId));
        
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                String.format(MSG_GRUPO_EM_USO, grupoId));
        }
    }
    

	@Autowired
	private PermissaoService cadastroPermissao;
	
	@Transactional
	public void desassociarPermissao(Long grupoId, Long permissaoId) {
	    Grupo grupo = buscarOuFalhar(grupoId);
	    Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);
	    
	    grupo.removerPermissao(permissao);
	}
	
	@Transactional
	public void associarPermissao(Long grupoId, Long permissaoId) {
	    Grupo grupo = buscarOuFalhar(grupoId);
	    Permissao permissao = cadastroPermissao.buscarOuFalhar(permissaoId);
	    
	    grupo.adicionarPermissao(permissao);
	} 

    public Grupo buscarOuFalhar(Long grupoId) {
        return grupoRepository.findById(grupoId)
            .orElseThrow(() -> new EntidadeEmUsoException(
                    String.format(MSG_GRUPO_EM_USO, grupoId)));
    }
}     
