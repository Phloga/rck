package de.vee.rck.units;

import de.vee.rck.units.dto.UnitDetails;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
public class UnitController {
    private UnitRepository unitRepo;
    private UnitMapper unitMapper;

    @GetMapping("/api/units/all")
    public List<UnitDetails> sendAllunits(){
        var result = StreamSupport.stream(unitRepo.findAll().spliterator(), false).map((item) -> {
            return unitMapper.unitToUnitDetails(item);
        }).collect(Collectors.toList());
        return result;
    }
}
