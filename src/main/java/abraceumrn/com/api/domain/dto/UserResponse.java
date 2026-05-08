package abraceumrn.com.api.domain.dto;

import abraceumrn.com.api.domain.enumItem.Role;

public record UserResponse(
        String username,
        Role role
) {
}
