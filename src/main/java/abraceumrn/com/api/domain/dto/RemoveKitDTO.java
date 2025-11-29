package abraceumrn.com.api.domain.dto;

import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.enumItem.KitType;

import java.util.List;

public record RemoveKitDTO(
        KitType type,
        List<KitDTO> items
) {
}
