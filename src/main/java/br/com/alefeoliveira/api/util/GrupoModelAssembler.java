package br.com.alefeoliveira.api.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.alefeoliveira.api.model.GrupoModel;
import br.com.alefeoliveira.domain.model.Grupo;

@Component
public class GrupoModelAssembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public GrupoModel toModel(Grupo grupo) {
        return modelMapper.map(grupo, GrupoModel.class);
    }
    
    public List<GrupoModel> toCollectionModel(Collection<Grupo> grupos) {
        return grupos.stream()
                .map(grupo -> toModel(grupo))
                .collect(Collectors.toList());
    }  
}
