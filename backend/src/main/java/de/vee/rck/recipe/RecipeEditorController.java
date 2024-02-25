package de.vee.rck.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.vee.rck.recipe.dto.PackedRecipe;
import de.vee.rck.recipe.dto.RecipeDetails;
import de.vee.rck.units.dto.UnitDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RecipeEditorController {

    private RecipeService recipeService;

    @GetMapping("/newRecipe")
    String openRecipeEditor(Model model){
        return "recipeEditor";
    }
    @GetMapping("/recipe/{id}")
    String openRecipe(@PathVariable("id") Long recipeId){
        //TODO open details view with given recipe
        return "recipeView";
    }

    @GetMapping("/recipe/{id}/edit")
    ModelAndView editRecipe(@PathVariable("id") Long recipeId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ModelMap modelMap = new ModelMap();
        String jsonText = mapper.writeValueAsString(recipeService.findAndPackRecipe(recipeId));
        modelMap.addAttribute("recipe", jsonText);
        modelMap.addAttribute("recipeId", recipeId);
        return new ModelAndView("recipeEditor", modelMap);
    }

    /**
     * Create a new recipe and redirect to the newly created resource (open editor)
     * @param recipe
     * @param authentication
     */
    @PreAuthorize("hasAuthority('MODIFY_RECIPE')")
    @PostMapping("/newRecipe")
    String createRecipe(@RequestBody PackedRecipe recipe, Authentication authentication) {
        Recipe recipeEntity = recipeService.updateRecipe(recipe, null, authentication.getName(), false);
        return MessageFormat.format("redirect:/recipe/{0}/edit", recipeEntity.getId());
    }


    @PreAuthorize("hasAuthority('MODIFY_RECIPE')")
    @PostMapping("/recipe/{id}")
    String saveRecipe(@PathVariable("id") Long recipeId, @RequestBody PackedRecipe recipe,
                    Authentication authentication)
    {
        boolean skipChecks = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        recipeService.updateRecipe(recipe, recipeId, authentication.getName(), skipChecks);
        return MessageFormat.format("redirect:/recipe/{0}/edit", recipeId);
    }
}
