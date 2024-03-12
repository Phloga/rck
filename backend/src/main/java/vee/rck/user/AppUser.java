package vee.rck.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vee.rck.recipe.Recipe;

import java.util.ArrayList;
import java.util.Collection;

@Entity @Setter @Getter @AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userName;
    private String email;
    private String password;
    private boolean enabled;
    private boolean tokenExpired;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<UserRole> roles;

    public boolean hasRole(long roleId){
        return getRoles().stream().anyMatch(
                role -> {return  role.getId() == roleId;});
    }

    /*
    @ManyToMany(mappedBy = "permittedUsers")
    private Collection<Recipe> recipes;
    */
    public AppUser(){
        this.enabled = false;
        this.tokenExpired = true;
        this.roles = new ArrayList<UserRole>();
        //this.recipes = new ArrayList<Recipe>();
    }
}