package vee.rck.units;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Unit {
    @Id @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;

    private String symbol;
    @Enumerated(value = EnumType.STRING)
    private UnitType type;
    private Float baseConversionMultiplier; //conversion rate to the respective cgs base units
}
