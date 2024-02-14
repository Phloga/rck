package de.vee.rck.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemListingDetails {
    private Long itemId;
    private String itemName;
    private Boolean isOptional;
    private String unit;
    private Float amount;
}
