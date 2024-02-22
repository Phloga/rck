package de.vee.rck.recipe;

import de.vee.rck.item.Item;
import de.vee.rck.units.Unit;
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
public class ItemListing implements Serializable {
    @EmbeddedId
    private ItemListingKey id;

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

    public ItemListing(Item item, Recipe recipe, Boolean isOutput){
        id = new ItemListingKey(recipe.getId(),item.getId(),isOutput);
        this.item = item;
        this.recipe = recipe;
    }
}
