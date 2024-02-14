package de.vee.rck.recipe;

import de.vee.rck.item.Item;
import de.vee.rck.item.ItemMapper;
import de.vee.rck.item.ItemRepository;
import de.vee.rck.item.ItemIdentifiers;
import de.vee.rck.recipe.dto.*;
import de.vee.rck.units.Unit;
import de.vee.rck.units.UnitRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
@AllArgsConstructor
public class RecipeMapper {

    private ItemRepository itemRepo;
    private UnitRepository unitRepo;
    private ItemMapper itemMapper;

    // create a new itemListing based on provided detail
    public static ItemListing createItemListing(ItemListingDetails details, boolean isOutput, Item item, Recipe recipe, Unit unit) {
        ItemListing listing = new ItemListing(item,recipe,isOutput);
        listing.setIsOptional(details.getIsOptional());
        listing.setAmount(details.getAmount());
        listing.setUnit(unit);
        return listing;
    }


    /** @param createItems : when true create item objects for listings with itemId set to null*/
    protected static ItemListing createItemListing(
            ItemListingDetails details, Recipe recipe, boolean isOutput, Map<Long, Item> id2item, Map<String, Unit> name2unit, boolean createItems) {
        Unit unit = name2unit.get(details.getUnit());
        if (unit == null) {
            throw new RecipeMappingFailure(MessageFormat.format(
                    "Unknown unit {0} used in ingredient list",details.getUnit()));
        }

        if (details.getItemId() == null){
            if (createItems){
                var item = new Item(details.getItemName(), false);
                return createItemListing(
                        details, isOutput, item , recipe, name2unit.get(details.getUnit()));
            } else {
                throw new RecipeMappingFailure(MessageFormat.format(
                        "Unknown item {0} in ingredient list",details.getItemName()));
            }
        } else {
            return createItemListing(
                    details, isOutput, id2item.get(details.getItemId()) , recipe, name2unit.get(details.getUnit()));
        }
    }

    /** fetches and sets item ids based on the unique item names of elements in listings
     *  This method finds ids with the help of ItemRepository
     * */
    protected void updateItemIds(Collection<ItemListingDetails> listings){
        // make a translation table for adding missing ids to details
        Collection<ItemIdentifiers> itemIdentifiers = itemRepo.findItemIdentifiersByNameIn(
                listings.stream().map(
                        ItemListingDetails::getItemName).collect(Collectors.toList()));
        Map<String, Long> itemName2Id = itemIdentifiers.stream().collect(
                Collectors.toMap(ItemIdentifiers::getName,ItemIdentifiers::getId));
        // add missing ids
        for (var listing : listings){
            listing.setItemId(itemName2Id.get(listing.getItemName()));
        }
    }
    protected static Stream<ItemListingDetails> findListingWithMissingIds(Stream<ItemListingDetails> listingDetails){
        return listingDetails.filter(
                (listing) -> {return listing.getItemId() == null;}
        );
    }

    @Transactional
    public Recipe toRecipe(RecipeDetails details){
        Recipe recipe = new Recipe();

        // copy simple attributes over
        recipe.setName(details.getName());
        recipe.setContent(details.getContent());

        // find listings without id and add missing ids
        var allListings = Stream.concat(details.getIngredients().stream(), details.getProducts().stream()).toList();
        updateItemIds(findListingWithMissingIds(allListings.stream()).toList());
        // map for fast lookup of items
        var itemIterator = itemRepo.findAllById(
                allListings.stream().map(ItemListingDetails::getItemId).filter(Objects::nonNull).collect(
                        Collectors.toList()));
        Map<Long, Item> id2item = StreamSupport.stream(
                itemIterator.spliterator(), false).collect(Collectors.toMap(
                Item::getId, (item) -> {return item;}));

        var unitStream = StreamSupport.stream(unitRepo.findAll().spliterator(), false);
        Map<String, Unit> name2unit = unitStream.collect(Collectors.toMap(
                Unit::getName, (unit) -> {return unit;}));

        // link items
        List<ItemListing> ingredientListings = details.getIngredients().stream().map((detailsListing)-> {
            return RecipeMapper.createItemListing(detailsListing, recipe, false, id2item,name2unit, false);
        }).toList();
        // link and create missing items
        List<ItemListing> productListings = details.getProducts().stream().map((detailsListing)-> {
            return RecipeMapper.createItemListing(detailsListing, recipe, true,id2item,name2unit,true);
        }).toList();

        recipe.setIngredientCount(
                (int)details.getIngredients().stream().filter(
                        (d) -> {return !d.getIsOptional();}
                ).count()
        );
        recipe.getItemLines().addAll(productListings);
        recipe.getItemLines().addAll(ingredientListings);
        return recipe;
    }
}
