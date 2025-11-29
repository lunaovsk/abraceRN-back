package abraceumrn.com.api.domain.strategy;

import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;

public interface KitCalcular {
    KitResponseDTO calcular(RemoveKitDTO kit);

    KitResponseDTO calcularKit(RemoveKitDTO kit);
}
