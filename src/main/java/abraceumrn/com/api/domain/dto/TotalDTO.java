package abraceumrn.com.api.domain.dto;

import abraceumrn.com.api.domain.items.RegisterItems;

public record TotalDTO(
        Integer total
) {
    public TotalDTO (RegisterItems i) {
        this(i.getQuantity());
    }
}
