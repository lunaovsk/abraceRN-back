package abraceumrn.com.api.domain.kit.strategy;

import abraceumrn.com.api.domain.enumItem.KitType;
import abraceumrn.com.api.infra.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KitFactory {

    @Autowired
    private EnxovalKitCalculator enxovalCalc;

    @Autowired
    private HigieneKitCalculator higieneCalc;

    public KitCalcular getCalculator(KitType type) {
        return switch (type) {
            case ENXOVAL -> enxovalCalc;
            case HIGIENE -> higieneCalc;
            default -> throw CustomException.validacao("Tipo de kit não suportado: " + type);
        };
    }
}