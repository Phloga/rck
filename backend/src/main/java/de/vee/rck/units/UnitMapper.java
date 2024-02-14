package de.vee.rck.units;

import de.vee.rck.units.dto.UnitDetails;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitMapper {
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "name"),
            @Mapping(target = "symbol"),
            @Mapping(target = "type"),
            @Mapping(target = "baseConversionMultiplier")
    })
    public Unit unitDetailsToUnit(UnitDetails details);
    public List<Unit> unitDetailsToUnit(Collection<UnitDetails> details);
    @InheritInverseConfiguration
    public UnitDetails unitToUnitDetails(Unit unit);
    public List<UnitDetails> unitToUnitDetails(Collection<Unit> units);
}
