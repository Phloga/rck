package de.vee.rck.item;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
    public Item findByName(String name);

    public Collection<Item> findByNameIn(Collection<String> names);

    public Collection<ItemIdentifiers> findItemIdentifiersByNameIn(Collection<String> names);

    @Query("select item from Item item where item.isBaseIngredient = ?1")
    public Collection<Item> findByIsBaseIngredient(boolean flag);

    //public findRecipesWithMatchingIngredients(Collection<Long> itemIds);
    //public Page<> findPagedRecipesWithMatchingIngredients(Collection<Long> itemIds);
}
