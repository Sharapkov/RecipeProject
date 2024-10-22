package com.pasta.mapper;

import com.pasta.dto.RecipeDto;
import com.pasta.model.Recipe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecipeMapper extends EntityMapper<RecipeDto, Recipe>{
}
