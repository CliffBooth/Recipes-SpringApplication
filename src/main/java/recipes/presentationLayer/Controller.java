package recipes.presentationLayer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.businessLayer.Recipe;
import recipes.businessLayer.RecipeService;
import recipes.businessLayer.User;
import recipes.businessLayer.UserDetailsServiceImpl;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired
    RecipeService recipeService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    PasswordEncoder encoder;


    @PostMapping("/recipe/new")
    public IdObject postRecipe(@RequestBody Recipe recipe, @AuthenticationPrincipal UserDetails details) {
        recipe.setEmail(details.getUsername());
        return new IdObject(recipeService.save(recipe).getId());
    }

    @GetMapping("/recipe/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        Optional<Recipe> res = recipeService.findRecipeById(id);
        return res.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/recipe/all")
    public List<Recipe> getAllRecipes() {
        return recipeService.findAll();
    }

    @GetMapping("/recipe/search")
    public List<Recipe> searchRecipe(@RequestParam Map<String, String> params) {
        String name = params.get("name");
        String category = params.get("category");
        if (name == null && category == null || params.size() > 2)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        List<Recipe> result = new ArrayList<>();
        if (name != null) {
            result.addAll(recipeService.findByNameAndSort(name));
            if (category != null) {
                result.removeIf(e -> !e.getCategory().equals(category));
            }
        } else {
            result.addAll(recipeService.findByCotegoryAndSort(category));
        }
        return result;
    }


    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long id, @AuthenticationPrincipal UserDetails details) {
        Optional<Recipe> recipe = recipeService.findRecipeById(id);
        if (recipe.isPresent()) {
            if (recipe.get().getEmail().equals(details.getUsername())) {
                recipeService.deleteRecipeById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/recipe/{id}")
    public ResponseEntity<String> putRecipe(
            @PathVariable Long id,
            @Valid @RequestBody Recipe recipe,
            @AuthenticationPrincipal UserDetails details
    ) {
        Optional<Recipe> opt = recipeService.findRecipeById(id);
        if (opt.isPresent()) {
            if (opt.get().getEmail().equals(details.getUsername())) {
                //just set the existing id and save the new recipe as an already existing one
                recipe.setId(opt.get().getId());
                recipe.setEmail(details.getUsername());
                recipeService.save(recipe);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid User user) {
        String email = user.getEmail();
        userDetailsService.findUserByEmail(email)
                .ifPresent((User u) -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
        user.setPassword(encoder.encode(user.getPassword()));
        userDetailsService.save(user);
    }

    @GetMapping("/allusers")
    public List<User> allUsers() {
        return userDetailsService.findAll();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public void conflict() {
    }
}

//wrapper for id, because we need to represent id as a json object.
@Data
@AllArgsConstructor
class IdObject {
    private final Long id;
}
