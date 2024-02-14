package de.vee.rck.item;

import de.vee.rck.item.dto.ItemDetails;
import de.vee.rck.units.Unit;
import de.vee.rck.units.dto.UnitDetails;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel="spring")
public interface ItemMapper {
    @Mappings({
            @Mapping(target = "id"),
            @Mapping(target = "name"),
            @Mapping(target = "isBaseIngredient")
    })
    public Item itemDetailsToItem(ItemDetails details);

    public List<Item> itemDetailsToItem(Collection<ItemDetails> details);
    @InheritInverseConfiguration
    public ItemDetails itemToItemDetails(Item unit);
    public List<ItemDetails> itemToItemDetails(Collection<Item> items);
}
