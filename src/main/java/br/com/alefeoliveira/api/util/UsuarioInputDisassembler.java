package br.com.alefeoliveira.api.util;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alefeoliveira.api.model.GrupoInput;
import br.com.alefeoliveira.api.model.UsuarioInput;
import br.com.alefeoliveira.domain.model.Grupo;
import br.com.alefeoliveira.domain.model.Usuario;

@Component
public class UsuarioInputDisassembler {

	@Autowired
    private ModelMapper modelMapper;
    
    public Usuario toDomainObject(UsuarioInput usuarioInput) {
        return modelMapper.map(usuarioInput, Usuario.class);
    }
    
    public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
        modelMapper.map(usuarioInput, usuario);
    } 
}  
