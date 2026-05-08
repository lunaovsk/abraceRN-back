package abraceumrn.com.api.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(
        @NotBlank
        String username,
        @NotBlank
        String password
) {
}
