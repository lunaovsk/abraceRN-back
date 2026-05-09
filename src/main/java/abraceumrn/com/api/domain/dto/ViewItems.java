package abraceumrn.com.api.domain.dto;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.Items;
import java.time.LocalDate;

public record ViewItems(
        Long id,
        String itemName,
        ItemType type,
        String size,
        Gender gender,
        LocalDate createdAt,
        LocalDate expirationAt,
        Integer quantity
) {
    public ViewItems(Items i) {
        this(
                i.getId(),
                i.getItemName(),
                i.getType(),
                i.getSize(),
                i.getGender(),
                i.getCreatedAt(),
                i.getExpirationAt(),
                i.getQuantity()
        );
    }
}
