package vee.rck.recipe;

import vee.rck.item.Item;
import vee.rck.units.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class RecipeLine implements Serializable {
    @EmbeddedId
    private RecipeLineKey id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    private Boolean isOptional;

    @ManyToOne
    private Unit unit;

    private Float amount;

    public Boolean getIsOutput(){
        return id.getIsOutput();
    }

    public void setIsOutput(Boolean flag){
        id.setIsOutput(flag);
    }

    public boolean isOutput() {
        return id.getIsOutput();
    }

    public boolean isInput() {
        return !id.getIsOutput();
    }

    /**
     * Constructor which extracts the key parameters from provided entities,
     * other properties are left uninitialized
     */
    public RecipeLine(Item item, Recipe recipe, Boolean isOutput){
        id = new RecipeLineKey(recipe.getId(),item.getId(),isOutput);
        this.item = item;
        this.recipe = recipe;
    }
}
