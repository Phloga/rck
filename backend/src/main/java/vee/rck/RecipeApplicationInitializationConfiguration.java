package vee.rck;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@Getter
@Setter
@ConfigurationProperties("rck.init")
public class RecipeApplicationInitializationConfiguration {

    private List<DataInitialization> data;

    private String usersLocation;

}

