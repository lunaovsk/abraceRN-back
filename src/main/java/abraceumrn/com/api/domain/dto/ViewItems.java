package abraceumrn.com.api.domain.dto;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.RegisterItems;
import java.time.LocalDate;

public record ViewItems(
        Long id,
        String itemName,
        ItemType type,
        String size,
        Gender gender,
        LocalDate createdAt,
        LocalDate expirationAt,
        int quantity

) {
    public ViewItems (RegisterItems i) {
        this(i.getId(), i.getItemName(), i.getType(), i.getSize(), i.getGender(), i.getCreatedAt(), i.getExpirationAt(), i.getQuantity());
    }
}
