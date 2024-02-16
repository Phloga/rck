package de.vee.rck.recipe;

import de.vee.rck.recipe.dto.RecipeQuery;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@AllArgsConstructor
public class RecipeApiController {

    private RecipeRepository recipeRepo;

    private RecipeMapper recipeMapper;

    @PostMapping(path="/findByIngredients",
            consumes="application/json",
            produces="application/json")
    public List<IngredientMatchingResult> findRecipesByIngredients(@RequestBody RecipeQuery query){
        var recipeList = recipeRepo.findRecipesByIngredientIds(query.getItemIds());
        return recipeList;
    }
}
