package abraceumrn.com.api.service;

import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.Item;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Page<Item> listItems(ItemType category, String itemName, String size, String type, Pageable pageable) {
        return itemRepository.findWithFilters(itemName, category, type, size, pageable);
    }
    
    public List<String> getTypesByCategory(ItemType category) {
        return itemRepository.findDistinctTypesByCategory(category);
    }

    public List<String> getAllItemNames() {
        return itemRepository.findAllDistinctItemNames();
    }

    public Integer totalOfItem() {
        Integer total = itemRepository.getSumOfAllQuantities();
        if (total == null) {
            return 0;
        }
        return total;
    }

    public void registerItems(ItemDTO dto) {
        itemRepository.save(new Item(dto));
    }

    public void deleteItem(Long id) {
        Item item = itemRepository.getReferenceById(id);
        item.deleteItem();
    }

    public void updateItemId(Long id, ItemDTO itemDTO) {
        Item item = itemRepository.getReferenceById(id);
        item.putItem(itemDTO);
    }
}