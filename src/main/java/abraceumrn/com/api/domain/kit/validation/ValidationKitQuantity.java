package abraceumrn.com.api.domain.kit.validation;

import abraceumrn.com.api.domain.dto.KitDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import abraceumrn.com.api.infra.exception.CustomException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Valida que a quantidade por kit de cada item é positiva.
 *
 * É uma validação de input: aplica-se tanto ao cálculo ({@code /calcular})
 * quanto à geração de kits, antes de qualquer consulta ao estoque.
 */
@Order(0)
@Component
public class ValidationKitQuantity implements ValidationKit {

    @Override
    public void validation(RemoveKitDTO dto) {
        for (KitDTO k : dto.items()) {
            if (k.quantity() == null || k.quantity() <= 0) {
                throw CustomException.validacao("Quantidade por kit inválida para: " + k.itemName());
            }
        }
    }
}
