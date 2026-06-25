package abraceumrn.com.api.domain.kit.validation;

import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import abraceumrn.com.api.domain.kit.strategy.KitFactory;
import abraceumrn.com.api.infra.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Valida que é possível montar ao menos um kit completo com o estoque atual.
 *
 * Reaproveita o cálculo da strategy (somatório por item) e, caso nenhum kit
 * seja possível, lança {@code KIT_INCOMPLETO} com os itens limitantes.
 */
@Order(2)
@Component
public class ValidationKitPossible implements ValidationKit {

    @Autowired
    private KitFactory factory;

    @Override
    public void validation(RemoveKitDTO dto) {
        KitResponseDTO calculo = factory.getCalculator(dto.type()).calcular(dto);

        if (calculo.kitsPossible() <= 0) {
            throw CustomException.kitIncompleto(calculo.limitingItems());
        }
    }
}