package de.vee.rck.recipe;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class RecipeEditorController {

    @GetMapping("/editRecipe")
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
}
