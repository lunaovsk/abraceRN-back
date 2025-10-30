package abraceumrn.com.api.domain.items;

import abraceumrn.com.api.domain.enumItem.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import abraceumrn.com.api.domain.enumItem.Gender;
import java.time.LocalDate;

public record ItemDTO(

        @NotBlank String itemName,
        @NotNull ItemType type,
        String size,
        @NotNull int quantity,
        Gender gender,
        LocalDate expirationAt
) {
}
