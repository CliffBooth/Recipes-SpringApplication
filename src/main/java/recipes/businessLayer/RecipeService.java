package recipes.businessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import recipes.persistanceLayer.RecipeRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecipeService {
     private final RecipeRepository recipeRepository;

     @Autowired
     public RecipeService(RecipeRepository recipeRepository) {
          this.recipeRepository = recipeRepository;
     }

     public Recipe save(Recipe recipe) {
          recipe.setDate(LocalDateTime.now());
          return recipeRepository.save(recipe);
     }

     public Optional<Recipe> findRecipeById(Long id) {
          return recipeRepository.findById(id);
     }

     public List<Recipe> findAll() {
          var it = recipeRepository.findAll();
          var users = new ArrayList<Recipe>();
          it.forEach(e -> users.add(e));
          return users;
     }

     public void deleteRecipeById(Long id) {
          recipeRepository.deleteById(id);
     }

     public List<Recipe> findByNameAndSort(String name) {
          return recipeRepository.findByNameContainingIgnoreCaseOrderByDateDesc(name);
     }

     public List<Recipe> findByCotegoryAndSort(String category) {
          return recipeRepository.findByCategoryIgnoreCaseOrderByDateDesc(category);
     }

}
