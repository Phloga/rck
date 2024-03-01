package de.vee.rck.recipe;

import de.vee.rck.item.Item;
import de.vee.rck.item.ItemRepository;
import de.vee.rck.recipe.dto.PackedRecipe;
import de.vee.rck.recipe.dto.RecipeDetails;
import de.vee.rck.security.AppUserDetailsService;
import de.vee.rck.user.AppUser;
import de.vee.rck.user.AppUserRepository;
import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.text.MessageFormat;
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
    private AppUserRepository userRepository;

    private AppUserDetailsService userDetailsService;

    public Collection<Item> findBaseIngredients(){
        return itemRepo.findByIsBaseIngredient(true);
    }


    // Transactional is important else there is a LazyInitializationException due to closed hibernate session
    @Transactional
    public PackedRecipe findAndPackRecipe(Long recipeId){
        return recipeMapper.toPackedRecipe(recipeRepo.findById(recipeId).orElseThrow());
    }

    /**
     *
     * @param updatedRecipe the updated/new recipe
     * @param recipeId  id of the affected recipe (null for create new)
     * @param userName  user who initiated the request
     * @return recipe entity after persisting
     */
    @Transactional
    public Recipe updateRecipe(RecipeDetails updatedRecipe,Long recipeId,String userName, boolean skipOwnerCheck){
        if (recipeId != null){
            Recipe recipe = recipeRepo.findById(recipeId).orElseThrow();
            AppUser owner = recipe.getOwner();
            if (skipOwnerCheck || (owner != null && owner.getUserName().equals(userName))) {
                Recipe recipeEntity = recipeMapper.toRecipe(updatedRecipe, recipeId, true, true);
                recipeEntity.setId(recipeId);
                recipeEntity.setOwner(owner);
                return recipeRepo.save(recipeEntity);
            }
            throw new RecipeAccessError(MessageFormat.format(
                    "Unauthorized attempt to update the recipe with id: {0}",recipeId));
        } else {
            Recipe recipeEntity = recipeMapper.toRecipe(updatedRecipe, null, true, true);
            AppUser user = userRepository.findByUserName(userName).orElseThrow();
            recipeEntity.setOwner(user);
            return recipeRepo.save(recipeEntity);
        }
    }
}
