package de.vee.rck.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RecipeQueryResponse implements RecipeDetails {
    private String owner;

    private String name;
    private String content;

    private Collection<ItemListingDetails> ingredients;
    private Collection<ItemListingDetails> products;

    public RecipeQueryResponse(RecipeDetails details, String owner) {
        this.copyFrom(details);
        this.owner = owner;
    }
}
