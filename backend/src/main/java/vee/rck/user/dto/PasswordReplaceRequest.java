package vee.rck.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PasswordReplaceRequest {
    String currentPassword;
    String newPassword;
}
