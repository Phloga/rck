package de.vee.rck.units.dto;

import de.vee.rck.units.UnitType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitDetails {
    private String name;
    private String symbol;
    private UnitType type;
    private Float baseConversionMultiplier; //conversion rate to the respective cgs base units
}
