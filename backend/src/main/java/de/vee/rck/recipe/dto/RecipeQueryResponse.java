package de.vee.rck.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class RecipeQueryResponse implements RecipeDetails {
    private String owner;

    private String name;
    private String content;

    private Collection<RecipeLineDTO> ingredients;
    private Collection<RecipeLineDTO> products;

    public RecipeQueryResponse(RecipeDetails details, String owner) {
        this.copyFrom(details);
        this.owner = owner;
    }
}
