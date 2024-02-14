package de.vee.rck.user;

import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    // Declare query methods here
    AppUser findByUserName(String userName);
    AppUser findByEmail(String email);
    boolean existsByUserName(String userName);
}