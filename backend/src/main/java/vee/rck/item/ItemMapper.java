package vee.rck.item;

import vee.rck.item.dto.ItemDTO;
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
    public Item itemDTOToItem(ItemDTO dto);

    public List<Item> itemDTOToItem(Collection<ItemDTO> dto);
    @InheritInverseConfiguration
    public ItemDTO itemToItemDTO(Item item);
    public List<ItemDTO> itemToItemDTO(Collection<Item> items);
}
