package de.vee.rck.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** itemId or itemName should be set and
 *  all other parameters should be set
 * */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemListingDetails {
    private Long itemId;
    private String itemName;
    private Boolean isOptional;
    // identifing units with a string allows easier handling of automatic unit creation
    private String unit;
    private Float amount;
}
