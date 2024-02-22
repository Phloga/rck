package de.vee.rck.recipe.dto;

import de.vee.rck.recipe.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PackedRecipe implements RecipeDetails {
    private String name;
    private String content;

    private Collection<ItemListingDetails> ingredients;
    private Collection<ItemListingDetails> products;
    public PackedRecipe(String title) {
        this.name = name;
        this.content = "";
        this.ingredients = new ArrayList<ItemListingDetails>();
        this.products = new ArrayList<ItemListingDetails>();
    }
}
