package abraceumrn.com.api.domain.items.validation;

import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.infra.exception.CustomException;
import org.springframework.stereotype.Component;

@Component
public class ValidationType implements ValidationItems {
    @Override
    public void validation(ItemDTO dto) {
        if (dto.type() == ItemType.ROUPA) {
            if (dto.size() == null || dto.gender() == null) {
                throw CustomException.validacao("Roupas devem conter tamanho e gênero.");
            }
        }
        if (dto.type() == ItemType.HIGIENE || dto.type() == ItemType.ALIMENTACAO) {
            if (dto.expirationAt() == null) {
                throw CustomException.validacao("Itens de higiene e alimentação devem conter data de validade.");
            }
        }
    }
}