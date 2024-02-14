package de.vee.rck.recipe;

public class RecipeMappingFailure extends RuntimeException {
    public RecipeMappingFailure(String errorMessage) {
        super(errorMessage);
    }
}
