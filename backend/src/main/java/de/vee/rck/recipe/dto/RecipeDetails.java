package de.vee.rck.recipe.dto;

import java.util.Collection;

public interface RecipeDetails {
    String getName();
    void setName(String name);

    String getContent();
    void setContent(String content);

    Collection<RecipeLineDTO> getIngredients();
    void setIngredients(Collection<RecipeLineDTO> listings);

    Collection<RecipeLineDTO> getProducts();
    void setProducts(Collection<RecipeLineDTO> listings);

    default void copyFrom(RecipeDetails details){
        this.setIngredients(details.getIngredients());
        this.setProducts(details.getProducts());
        this.setContent(details.getContent());
        this.setName(details.getName());
    }

}
