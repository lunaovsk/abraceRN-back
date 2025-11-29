package abraceumrn.com.api.domain.dto;

import java.util.Map;

public record KitResponseDTO(
        Integer kitsPossible,
        Map<String, Integer> limitingItems

) {}