package com.pasta.service;

import com.pasta.model.Recipe;
import com.pasta.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;



    public RecipeService(RecipeRepository recipeRepository){
        this.recipeRepository = recipeRepository;
    }
    public RecipeRepository getAllRecipes(){
        List<Recipe> recipeAll = new ArrayList<>();
        recipeRepository.findAll().forEach(recipeAll::add);
        return (RecipeRepository) recipeAll;
    }
    public Optional<Recipe> findRecipeById(Long recipeId){
        if(recipeRepository.findById(recipeId).isPresent()){
            return Optional.of(recipeRepository.findById(recipeId).get());
        }else {
            return Optional.empty();
        }
    }
    public Long saveRecipe(Recipe toSave){
        return recipeRepository.save(toSave).getId();
    }
    public void deleteRecipe(Long recipeId){
        recipeRepository.deleteById(recipeId);
    }
}
