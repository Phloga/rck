package vee.rck.recipe;

import vee.rck.security.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
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

    private String name;
    @Column(name="content", columnDefinition="TEXT")
    private String content;

    /** ingredients and produced products of the recipe */
    @OneToMany(mappedBy = "recipe",cascade = CascadeType.ALL)
    private Collection<RecipeLine> itemLines;

    /** Should be set to the occurrences of ItemListings in itemLines with isOptional and isOutput set to false
     *  ,This property should help in avoiding some queries on the item_listing table */
    private Integer ingredientCount;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private AppUser owner;

    /*
    @ManyToMany
    @JoinTable(
            name = "recipes_users",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "recipe_id", referencedColumnName = "id"))
    private Collection<AppUser> permittedUsers;
    */
    public Recipe(){
        name = "";
        content = "";
        itemLines = new ArrayList<>();
        ingredientCount = 0;
    }
}
