package abraceumrn.com.api.domain.dto;

public record TotalDTO(
        Integer total,
        Integer totalType,
        Integer totalTypeDistinct,
        Integer totalUnique
) {
}
