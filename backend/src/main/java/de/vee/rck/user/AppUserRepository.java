package de.vee.rck.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    // Declare query methods here
    Optional<AppUser> findByUserName(String userName);
    Optional<AppUser> findByEmail(String email);
    boolean existsByUserName(String userName);

    @Query(value="select count(*) from users_roles where role_id = :roleId", nativeQuery = true)
    int countByRole(Long roleId);
}