package de.vee.rck.recipe;

import de.vee.rck.item.Item;
import de.vee.rck.item.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * This class can Query and manipulate the recipe database by directly using data transfer objects
 */
@Service @AllArgsConstructor
public class RecipeService {
    private ItemRepository itemRepo;
    public Collection<Item> findBaseIngredients(){
        return itemRepo.findByIsBaseIngredient(true);
    }
}
