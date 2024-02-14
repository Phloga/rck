package de.vee.rck.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AvailabilityCheckRequest {
    /** data to check e.g. a name or email address*/
    private String value;
}
