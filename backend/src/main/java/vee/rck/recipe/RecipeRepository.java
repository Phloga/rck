package vee.rck.recipe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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
    @Query(value= "SELECT recipe_id AS recipeId,name AS recipeName,ingredient_matches AS ingredientMatches,ingredient_count as ingredientCount FROM\n" +
            "(SELECT recipe_id,count(*) AS ingredient_matches FROM\n" +
            "(SELECT recipe_id FROM recipe_line WHERE item_id IN (:itemIds))" +
            "GROUP BY recipe_id) matches INNER JOIN recipe ON recipe.id=recipe_id\n", nativeQuery = true)
    List<IngredientMatchingResult> findRecipesByIngredientIds(Collection<Long> itemIds);


    @Query(value = "SELECT r from Recipe r where r.owner.id=:ownerId")
    List<Recipe> findRecipesByOwnerId(Long ownerId);
}
