package vee.rck.recipe;

import vee.rck.recipe.dto.RecipeQuery;
import vee.rck.recipe.dto.RecipeQueryResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class RecipeApiController {

    private RecipeRepository recipeRepo;

    private RecipeMapper recipeMapper;

    /*
    @PostMapping(path="/api/recipes/recipes-by-ingredients",
            consumes="application/json",
            produces="application/json")
    public List<IngredientMatchingResult> findRecipesByIngredients(@RequestBody RecipeQuery query){
        return recipeRepo.findRecipesByIngredientIds(query.getItemIds());
    }*/

    @GetMapping(path="/api/recipes/by-ingredients", produces="application/json")
    public List<IngredientMatchingResult> findRecipesByIngredients(@RequestParam List<Long> items){
        return recipeRepo.findRecipesByIngredientIds(items);
    }


    @GetMapping(path="/api/recipes/d/{id}",produces="application/json")
    RecipeQueryResponse sendRecipe(@PathVariable("id") Long recipeId){
        var recipe = recipeRepo.findById(recipeId).orElseThrow();
        return new RecipeQueryResponse(recipeMapper.toPackedRecipe(recipe), recipe.getOwner().getUserName());
    }
}
