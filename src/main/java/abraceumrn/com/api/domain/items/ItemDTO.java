package abraceumrn.com.api.domain.items;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ItemDTO(
        @NotBlank 
        String itemName,
        
        @NotNull 
        ItemType category,
        
        String type,
        
        String size,
        
        @NotNull 
        Integer quantity,
        
        Gender gender,
        
        LocalDate expirationAt
) {
}