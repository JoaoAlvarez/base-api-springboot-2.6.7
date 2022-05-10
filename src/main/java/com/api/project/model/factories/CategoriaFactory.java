package com.api.project.model.factories;

import com.api.project.model.entity.Categoria;
import com.api.project.model.dto.CategoriaDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriaFactory {
    public static List<CategoriaDTO> toDto(List<Categoria> list){
        return list.stream().map(el -> toDto(el)).collect(Collectors.toList());
    }

    public static CategoriaDTO toDto(Categoria obj) {
        return CategoriaDTO.builder()
                .id(obj.getId())
                .nome(obj.getNome())
                .build();
    }

    public static Categoria fromDTO(CategoriaDTO dto) {
        return new Categoria(dto.getId(),dto.getNome());
    }
}
