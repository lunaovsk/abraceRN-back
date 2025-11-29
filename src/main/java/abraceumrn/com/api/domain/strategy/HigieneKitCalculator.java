package abraceumrn.com.api.domain.strategy;

import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import org.springframework.stereotype.Service;

@Service
public class HigieneKitCalculator extends AbstractKitCalculator {

    @Override
    public KitResponseDTO calcular(RemoveKitDTO kit) {
        return calcularKit(kit);
    }
}