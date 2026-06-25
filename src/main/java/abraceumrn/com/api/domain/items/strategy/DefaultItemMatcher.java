package abraceumrn.com.api.domain.items.strategy;

import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.items.Items;
import abraceumrn.com.api.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Matcher padrão para itens não perecíveis (Roupa, Acessório, etc.).
 *
 * A unicidade é definida por tipo, nome, tamanho e gênero.
 */
@Component
public class DefaultItemMatcher implements ItemMatcher {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Items findExisting(ItemDTO dto) {
        return itemRepository.findByTypeAndItemNameAndSizeAndGender(
                dto.type(), dto.itemName(), dto.size(), dto.gender());
    }

    @Override
    public boolean isSame(Items item, ItemDTO dto) {
        return item.getType().equals(dto.type())
                && item.getItemName().equals(dto.itemName())
                && Objects.equals(item.getSize(), dto.size())
                && Objects.equals(item.getGender(), dto.gender());
    }
}