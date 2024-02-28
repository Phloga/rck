package de.vee.rck.recipe;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
public class RecipeLineKey implements Serializable {
    @Column(name = "recipe_id")
    private Long recipeId;

    @Column(name = "item_id")
    private Long itemId;

    /** a recipe could theoretically produce one of its inputs as output,
     * including this flag in the key allows such constructs to exist
     */
    @Column(name = "is_output")
    private Boolean isOutput;
}
