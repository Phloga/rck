package vee.rck.recipe;

import vee.rck.RecipeApplication;
import vee.rck.item.Item;
import vee.rck.item.ItemRepository;
import vee.rck.units.UnitRepository;
import vee.rck.security.AppUserRepository;
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
    static final int expectedMatches = 2;

    static final long recipeOwnerId = 1; //admin
    @Autowired
    UnitRepository unitRepo;
    @Autowired
    ItemRepository itemRepo;
    @Autowired
    RecipeRepository recipeRepo;
    @Autowired
    AppUserRepository userRepo;

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
    }

    @Test
    void findUserAndFindRecipesByUser() {
        var user = userRepo.findByUserName("Admin");
        assertTrue(user.isPresent());
        var recipeList = recipeRepo.findRecipesByOwnerId(user.get().getId());
        assertEquals(1, recipeList.size());
    }
}
