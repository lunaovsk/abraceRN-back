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
 * Valida que todos os itens informados no kit existem no estoque.
 *
 * Executa antes das demais validações para que um item inexistente
 * gere {@code ITEM_NAO_ENCONTRADO} em vez de mascarar como kit incompleto.
 */
@Order(1)
@Component
public class ValidationKitItemExists implements ValidationKit {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void validation(RemoveKitDTO dto) {
        for (KitDTO k : dto.items()) {
            Items item = itemRepository.findByItemNameAndSizeAndGender(
                    k.itemName(), k.size(), k.gender());

            if (item == null) {
                throw CustomException.itemNaoEncontrado(
                        k.itemName() + " - Tamanho: " + k.size() + " - Gênero: " + k.gender()
                );
            }
        }
    }
}
