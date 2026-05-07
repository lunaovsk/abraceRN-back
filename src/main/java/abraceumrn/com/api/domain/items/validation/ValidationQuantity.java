package abraceumrn.com.api.domain.items.validation;

import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.infra.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class ValidationQuantity implements ValidationItems {
    @Override
    public void validation(ItemDTO dto) {
        if (dto.quantity() < 0) {
            throw CustomException.validacao("Quantidade deve ser maior ou igual a 0.");
        }
    }
}