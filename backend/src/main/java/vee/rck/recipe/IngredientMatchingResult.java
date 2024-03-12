package vee.rck.recipe;

public interface IngredientMatchingResult {
    Long getRecipeId();
    String getRecipeName();
    Integer getIngredientMatches();
    Integer getIngredientCount();
}
