package recipes.persistanceLayer;

import org.springframework.data.repository.CrudRepository;
import recipes.businessLayer.Recipe;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    List<Recipe> findByNameContainingIgnoreCaseOrderByDateDesc(String name);

    List<Recipe> findByCategoryIgnoreCaseOrderByDateDesc(String category);
}
