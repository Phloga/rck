package de.vee.rck.recipe.dto;

import java.util.Collection;

public interface RecipeDetails {
    String getName();
    void setName(String name);

    String getContent();
    void setContent(String content);

    Collection<ItemListingDetails> getIngredients();
    void setIngredients(Collection<ItemListingDetails> listings);

    Collection<ItemListingDetails> getProducts();
    void setProducts(Collection<ItemListingDetails> listings);

    default void copyFrom(RecipeDetails details){
        this.setIngredients(details.getIngredients());
        this.setProducts(details.getProducts());
        this.setContent(details.getContent());
        this.setName(details.getName());
    }

}
