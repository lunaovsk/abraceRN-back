package abraceumrn.com.api.domain.user;

import abraceumrn.com.api.domain.enumItem.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @NotNull
        Role role
) {
}
