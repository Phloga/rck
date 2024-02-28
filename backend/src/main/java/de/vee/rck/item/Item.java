package de.vee.rck.item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique=true)
    private String name;

    /** Marks items that are primarily used in other recipes */
    private Boolean isBaseIngredient;

    public Item(String name, boolean isBaseIngredient){
        this.name = name;
        this.isBaseIngredient = isBaseIngredient;
    }
}
