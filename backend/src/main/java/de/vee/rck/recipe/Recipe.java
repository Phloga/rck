package de.vee.rck.recipe;

import de.vee.rck.user.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Recipe implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String content;

    /** ingredients and produced products of the recipe */
    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL)
    private Collection<ItemListing> itemLines;

    /** Should be set to the occurrences of ItemListings in itemLines with isOptional and isOutput set to false
     *  ,This property should help in avoiding some queries on the item_listing table */
    private Integer ingredientCount;

    @ManyToMany
    @JoinTable(
            name = "recipes_users",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "recipe_id", referencedColumnName = "id"))
    private Collection<AppUser> permittedUsers;

    public Recipe(){
        name = "";
        content = "";
        itemLines = new ArrayList<>();
    }
}
