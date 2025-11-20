package abraceumrn.com.api.domain.dto;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.Item;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ViewItems(
        Long id,
        String itemName,
        ItemType type,
        String size,
        Gender gender,
        LocalDateTime createdAt,
        LocalDate expirationAt,
        Integer quantity
) {
    public ViewItems(Item item) {
        this(
            item.getId(),
            item.getItemName(),
            item.getCategory(),
            item.getSize(),
            item.getGender(),
            item.getCreate_at(),
            item.getExpiration_at(),
            item.getQuantity()
        );
    }
}