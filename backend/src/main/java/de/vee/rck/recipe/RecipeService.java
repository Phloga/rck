package de.vee.rck.recipe;

import de.vee.rck.item.Item;
import de.vee.rck.item.ItemRepository;
import de.vee.rck.recipe.dto.PackedRecipe;
import de.vee.rck.recipe.dto.RecipeDetails;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * This service implements business logic and isolates the database from the controllers,
 */
@Service @AllArgsConstructor
public class RecipeService {
    private ItemRepository itemRepo;
    private SessionFactory sessionFactory;

    private RecipeRepository recipeRepo;
    private RecipeMapper recipeMapper;

    public Collection<Item> findBaseIngredients(){
        return itemRepo.findByIsBaseIngredient(true);
    }


    // Transactional is important else there is a LazyInitializationException due to closed hibernate session
    @Transactional
    public PackedRecipe findAndPackRecipe(Long recipeId){
        return recipeMapper.toPackedRecipe(recipeRepo.findById(recipeId).orElseThrow());
    }
}
