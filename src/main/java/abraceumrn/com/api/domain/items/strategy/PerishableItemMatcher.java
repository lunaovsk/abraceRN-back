package abraceumrn.com.api.domain.items.strategy;

import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.items.Items;
import abraceumrn.com.api.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Matcher para itens perecíveis (Higiene e Alimentação).
 *
 * A unicidade é definida por tipo, nome e data de validade, considerando também
 * tamanho e gênero quando esses campos estiverem preenchidos (ex.: fralda P/M/G).
 * Campos nulos são tratados como "sem tamanho/gênero" e casam entre si.
 */
@Component
public class PerishableItemMatcher implements ItemMatcher {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Items findExisting(ItemDTO dto) {
        return itemRepository.findByTypeAndItemNameAndSizeAndGenderAndExpirationAt(
                dto.type(), dto.itemName(), dto.size(), dto.gender(), dto.expirationAt());
    }

    @Override
    public boolean isSame(Items item, ItemDTO dto) {
        return item.getType().equals(dto.type())
                && item.getItemName().equals(dto.itemName())
                && Objects.equals(item.getSize(), dto.size())
                && Objects.equals(item.getGender(), dto.gender())
                && Objects.equals(item.getExpirationAt(), dto.expirationAt());
    }
}