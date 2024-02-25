package de.vee.rck.recipe;

import lombok.Getter;

public class RecipeAccessError extends RuntimeException {
    public RecipeAccessError(String message){
        super(message);
    }
}
