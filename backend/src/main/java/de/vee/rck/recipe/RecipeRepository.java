package de.vee.rck.recipe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    @Query(value = "FROM (Select recipe_id, count(*))", nativeQuery = true)
    List<IngredientMatchingResultImpl> countTotalItemsGroupedByRecipe();

    /** Find Recipes based on a list of ingredients
     * @param itemIds ids of the ingredients
     * @return List of objects containing recipes' id,name ingredient matches and ingredient counts,
     * where ingredient matches equals the number of intersections of itemIds with the recipe's ingredient list
     */

    //broken JPQL trash
    /*@Query(value= "SELECT new de.vee.rck.recipe.IngredientMatchingResultImpl(recipe_id,name,ingredient_matches,ingredient_count) FROM\n" +
            "(SELECT recipe_id AS recipe_id,count(*) AS ingredient_matches FROM\n" +
            "(SELECT il.id.recipeId AS recipe_id FROM ItemListing il WHERE il.id.itemId IN (?1)) GROUP BY il.id.recipeId) matches\n" +
            "INNER JOIN Recipe recipe ON recipe.id=matches.recipeId")
    */
    @Query(value= "SELECT recipe_id AS recipeId,name AS recipeName,ingredient_matches AS ingredientMatches,ingredient_count as ingredientCount FROM\n" +
            "(SELECT recipe_id,count(*) AS ingredient_matches FROM\n" +
            "(SELECT recipe_id FROM item_listing WHERE item_id IN (:itemIds))" +
            "GROUP BY recipe_id) matches INNER JOIN recipe ON recipe.id=recipe_id\n", nativeQuery = true)
    List<IngredientMatchingResult> findRecipesByIngredientIds(Collection<Long> itemIds);
}
