package de.vee.rck.recipe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.vee.rck.units.dto.UnitDetails;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RecipeEditorController {

    private RecipeService recipeService;

    @GetMapping("/newRecipe")
    String openRecipeEditor(@RequestParam Optional<Long> recipeId, Model model){
        if (recipeId.isPresent()) {
            //TODO open existing recipe
            int x = 0;
        } else {
            //TODO create new recipe
            int x = 0;
        }
        return "recipeEditor";
    }
    @GetMapping("/recipe/{id}")
    String openRecipe(@PathVariable("id") Long recipeId){
        //TODO open editor with given recipe
        return "recipeView";
    }

    @GetMapping("/recipe/{id}/edit")
    ModelAndView editRecipe(@PathVariable("id") Long recipeId) throws JsonProcessingException {
        //TODO open editor with given recipe
        ObjectMapper mapper = new ObjectMapper();
        String jsonText = mapper.writeValueAsString(recipeService.findAndPackRecipe(recipeId));
        return new ModelAndView("recipeEditor", "recipe", jsonText);
    }
}
