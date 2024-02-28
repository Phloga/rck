package de.vee.rck.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.vee.rck.recipe.dto.PackedRecipe;
import de.vee.rck.recipe.dto.RecipeDetails;
import de.vee.rck.units.dto.UnitDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URI;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/recipe")
public class RecipeEditorController {

    private RecipeService recipeService;

    private static final String recipeUriPrefix = "/recipe/d";

    @GetMapping("/newRecipe")
    String openRecipeEditor(Model model){
        return "recipeEditor";
    }
    @GetMapping("/d/{id}")
    ModelAndView openRecipe(@PathVariable("id") Long recipeId) throws JsonProcessingException {
        ObjectMapper jsonMapper = new ObjectMapper();
        ModelMap modelMap = new ModelMap();
        String jsonText = jsonMapper.writeValueAsString(recipeService.findAndPackRecipe(recipeId));
        modelMap.addAttribute("recipe", jsonText);
        modelMap.addAttribute("recipeId", recipeId);
        return new ModelAndView("recipeView", modelMap);
    }

    @PreAuthorize("hasAuthority('MODIFY_RECIPE')")
    @GetMapping("/d/{id}/edit")
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
    ResponseEntity<String> createRecipe(@RequestBody PackedRecipe recipe, Authentication authentication) {
        Recipe recipeEntity = recipeService.updateRecipe(recipe, null, authentication.getName(), false);
        //respond with 201, put link to resource in Location header
        URI location = URI.create(MessageFormat.format("/recipe/d/{0}/edit", recipeEntity.getId()));
        return ResponseEntity.created(location).build();
    }


    @PreAuthorize("hasAuthority('MODIFY_RECIPE')")
    @PostMapping("/d/{id}")
    void saveRecipe(@PathVariable("id") Long recipeId, @RequestBody PackedRecipe recipe,
                    Authentication authentication)
    {
        boolean skipChecks = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        recipeService.updateRecipe(recipe, recipeId, authentication.getName(), skipChecks);
        //URI location = URI.create(MessageFormat.format("/recipe/d/{0}/edit", recipeId));
        //return ResponseEntity.ok();
    }
}
