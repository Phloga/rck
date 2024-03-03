package de.vee.rck.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PackedRecipe implements RecipeDetails {
    private String name;
    private String content;

    private Collection<RecipeLineDTO> ingredients;
    private Collection<RecipeLineDTO> products;
    public PackedRecipe(String title) {
        this.name = name;
        this.content = "";
        this.ingredients = new ArrayList<RecipeLineDTO>();
        this.products = new ArrayList<RecipeLineDTO>();
    }
}
