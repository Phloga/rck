package vee.rck.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientMatchingResultImpl implements IngredientMatchingResult{
    private Long recipeId;
    private String recipeName;
    private Integer ingredientMatches;
    private Integer ingredientCount;

    public IngredientMatchingResultImpl(IngredientMatchingResult result){
        this(result.getRecipeId(),result.getRecipeName(), result.getIngredientMatches(), result.getIngredientCount());
    }
}
