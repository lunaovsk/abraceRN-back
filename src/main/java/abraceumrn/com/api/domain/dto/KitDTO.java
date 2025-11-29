package abraceumrn.com.api.domain.dto;

import abraceumrn.com.api.domain.enumItem.Gender;

import java.util.List;

public record KitDTO(

        String itemName,
        Gender gender,
        String size,
        Integer quantity
) {
}
