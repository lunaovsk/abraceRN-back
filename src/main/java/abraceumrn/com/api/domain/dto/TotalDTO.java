package abraceumrn.com.api.domain.dto;

import java.util.Map;

public record TotalDTO(
        Integer total,
        Integer totalType,
        Integer totalTypeDistinct,
        Integer totalUnique

) {
}
