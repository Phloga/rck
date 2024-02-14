package de.vee.rck.units;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface UnitRepository extends CrudRepository<Unit,Long> {
}
