package ProjectRecipe.demo;

import jakarta.validation.Valid;
import lombok.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	public static class Recipe{
		public String name;
		public String description;
		public String ingredients;
		public String directions;

		public Recipe(String name, String description, String ingredients, String directions) {
			this.name = name;
			this.description = description;
			this.ingredients = ingredients;
			this.directions = directions;
		}
	}
	Recipe r;

	@RestController
	@Validated
	public class recipeController {


		@PostMapping("/api/recipe/")
		public void recipePost(@Valid @RequestBody Recipe recipe) {
			Recipe rn = new Recipe(recipe.name, recipe.description, recipe.ingredients, recipe.directions);
			r = rn;
		}



		@GetMapping("/api/recipe/")
		public Recipe recipe (){
			return r;
		}

	}
}

