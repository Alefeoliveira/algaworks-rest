package br.com.alefeoliveira.api.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alefeoliveira.api.model.GrupoModel;
import br.com.alefeoliveira.api.model.UsuarioModel;
import br.com.alefeoliveira.domain.model.Grupo;
import br.com.alefeoliveira.domain.model.Usuario;

@Component
public class UsuarioModelAssembler {


    @Autowired
    private ModelMapper modelMapper;
    
    public UsuarioModel toModel(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioModel.class);
    }
    
    public List<UsuarioModel> toCollectionModel(Collection<Usuario> usuarios) {
        return usuarios.stream()
                .map(usuario -> toModel(usuario))
                .collect(Collectors.toList());
    }   
}
