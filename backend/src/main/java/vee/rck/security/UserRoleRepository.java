package vee.rck.security;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    public Optional<UserRole> findByName(String name);
}
