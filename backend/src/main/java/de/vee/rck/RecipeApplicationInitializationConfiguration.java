package de.vee.rck;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties("rck.init")
public class RecipeApplicationInitializationConfiguration {
    private String recipesLocation;
    private String itemsLocation;
    private String unitsLocation;
}
