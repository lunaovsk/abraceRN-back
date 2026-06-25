package abraceumrn.com.api.domain.items.strategy;

import abraceumrn.com.api.domain.enumItem.ItemType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Resolve qual {@link ItemMatcher} usar conforme o tipo do item.
 *
 * Segue o mesmo padrão de fábrica utilizado para os calculadores de kit.
 */
@Service
public class ItemMatcherFactory {

    @Autowired
    private PerishableItemMatcher perishableMatcher;

    @Autowired
    private DefaultItemMatcher defaultMatcher;

    public ItemMatcher getMatcher(ItemType type) {
        return switch (type) {
            case HIGIENE, ALIMENTACAO -> perishableMatcher;
            default -> defaultMatcher;
        };
    }
}