package abraceumrn.com.api.domain.kit.validation;

import abraceumrn.com.api.domain.dto.KitDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import abraceumrn.com.api.domain.items.Items;
import abraceumrn.com.api.domain.repository.ItemRepository;
import abraceumrn.com.api.infra.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Valida que o registro de cada item possui quantidade suficiente para o kit.
 *
 * Diferente de {@link ValidationKitPossible}, compara a quantidade do registro
 * específico (nome + tamanho + gênero) com o necessário, lançando
 * {@code ESTOQUE_INSUFICIENTE} quando não houver saldo.
 */
@Order(3)
@Component
public class ValidationKitStock implements ValidationKit {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void validation(RemoveKitDTO dto) {
        for (KitDTO k : dto.items()) {
            Items item = itemRepository.findByItemNameAndSizeAndGender(
                    k.itemName(), k.size(), k.gender());

            if (item != null && item.getQuantity() < k.quantity()) {
                throw CustomException.estoqueInsuficiente(
                        k.itemName(), item.getQuantity(), k.quantity()
                );
            }
        }
    }
}