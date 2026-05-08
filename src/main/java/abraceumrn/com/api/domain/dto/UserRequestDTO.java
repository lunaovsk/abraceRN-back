package abraceumrn.com.api.domain.dto;

import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
