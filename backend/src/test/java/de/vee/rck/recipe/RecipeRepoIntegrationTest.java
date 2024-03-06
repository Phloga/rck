package de.vee.rck.recipe;

import de.vee.rck.RecipeApplication;
import de.vee.rck.item.Item;
import de.vee.rck.item.ItemRepository;
import de.vee.rck.units.UnitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RecipeApplication.class)
public class RecipeRepoIntegrationTest {
    // item ids from data.sql
    static final int expectedMatches = 2;

    static final long recipeOwnerId = 1; //admin
    @Autowired
    UnitRepository unitRepo;
    @Autowired
    ItemRepository itemRepo;
    @Autowired
    RecipeRepository recipeRepo;

    boolean equalNumericalMembers(IngredientMatchingResult a, IngredientMatchingResult b){
        return a.getRecipeId().equals(b.getRecipeId()) &&
                a.getIngredientCount().equals(b.getIngredientCount()) &&
                a.getIngredientMatches().equals(b.getIngredientMatches());
    }
    @Test
    @WithMockUser(value = "admin")
    void testRequestChainFindRecipes(){
        var ingredients = itemRepo.findByIsBaseIngredient(true);
        assertFalse(ingredients.isEmpty());
        assertTrue(ingredients.stream().allMatch(Item::getIsBaseIngredient));

        var ingredientsMap = ingredients.stream().collect(Collectors.toMap(Item::getName, Function.identity()));

        List<Long> filterList = Arrays.asList(ingredientsMap.get("Zucker").getId(),ingredientsMap.get("Milch").getId());
        List<IngredientMatchingResultImpl> results = recipeRepo.findRecipesByIngredientIds(filterList)
                .stream().map(IngredientMatchingResultImpl::new).toList();

        assertEquals(expectedMatches, results.size());
        for (var result : results){
            var recipe = recipeRepo.findById(result.getRecipeId());
            assertTrue(recipe.isPresent());
        }
        /*

        List<IngredientMatchingResult> expectedResults = Arrays.asList(
                new IngredientMatchingResultImpl(0L, "Salzige Milch", 1, 2),
                new IngredientMatchingResultImpl(1L, "Süße Milch", 2, 2));

        assertEquals(expectedResults.size(), results.size());
        assertTrue(results.stream().anyMatch((r) -> {
            return equalNumericalMembers(r, expectedResults.get(0));
        }));
        assertTrue(results.stream().anyMatch((r) -> {
            return equalNumericalMembers(r, expectedResults.get(1));
        }));

        */
    }

    @Test
    void findRecipesByUser() {
        var recipeList = recipeRepo.findRecipesByOwnerId(recipeOwnerId);
        assertEquals(1, recipeList.size());
    }
}
