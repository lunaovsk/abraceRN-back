package abraceumrn.com.api.domain.dto;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType; 
import abraceumrn.com.api.domain.items.Item;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ViewItems(
        String itemName,
        ItemType category, 
        String type,
        String size,
        Gender gender,
        LocalDate expiration_at,
        Integer quantity,
        LocalDateTime create_at,
        Long id
) {

    public ViewItems(Item item) {
        this(
                item.getItemName(),
                item.getCategory(),
                item.getType(),
                item.getSize(),
                item.getGender(),
                item.getExpiration_at(),
                item.getQuantity(),
                item.getCreate_at(),
                item.getId()
        );
    }
}