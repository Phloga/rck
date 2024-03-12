package vee.rck.units;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UnitRepository extends CrudRepository<Unit,Long> {
}
