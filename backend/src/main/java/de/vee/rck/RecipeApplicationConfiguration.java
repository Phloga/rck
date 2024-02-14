package de.vee.rck;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class RecipeApplicationConfiguration {
    @Value( "${rck.loadSampleData}" )
    private Boolean loadSampleData;
}
