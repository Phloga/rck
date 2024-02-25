package de.vee.rck.recipe;

import de.vee.rck.recipe.dto.RecipeQuery;
import de.vee.rck.recipe.dto.RecipeQueryResponse;
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
        return recipeRepo.findRecipesByIngredientIds(query.getItemIds());
    }


    @GetMapping(path="/data/{id}",produces="application/json")
    RecipeQueryResponse sendRecipe(@PathVariable("id") Long recipeId){
        var recipe = recipeRepo.findById(recipeId).orElseThrow();
        return new RecipeQueryResponse(recipeMapper.toPackedRecipe(recipe), recipe.getOwner().getUserName());
    }
}
