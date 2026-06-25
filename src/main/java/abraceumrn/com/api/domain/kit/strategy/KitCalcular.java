package abraceumrn.com.api.domain.kit.strategy;

import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;

/**
 * Estratégia de cálculo de kits.
 *
 * Cada tipo de kit ({@code KitType}) tem uma implementação resolvida pelo
 * {@link KitFactory}, permitindo regras de cálculo específicas por tipo.
 */
public interface KitCalcular {
    KitResponseDTO calcular(RemoveKitDTO kit);
}
