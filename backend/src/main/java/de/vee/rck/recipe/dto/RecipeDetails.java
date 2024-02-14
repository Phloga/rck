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
public class RecipeDetails {
    private String name;
    private String content;

    private Collection<ItemListingDetails> ingredients;
    private Collection<ItemListingDetails> products;
    public RecipeDetails(String title) {
        this.name = name;
        this.content = "";
        this.ingredients = new ArrayList<ItemListingDetails>();
        this.products = new ArrayList<ItemListingDetails>();
    }
}
