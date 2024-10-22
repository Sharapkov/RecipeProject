package com.pasta.repository;

import com.pasta.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  RecipeRepository extends JpaRepository<Recipe, Long> {


}
