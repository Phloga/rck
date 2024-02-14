package de.vee.rck.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetails implements Serializable {
    private Long id;
    private String name;
    private Boolean isBaseIngredient;
}