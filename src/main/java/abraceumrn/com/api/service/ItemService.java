package abraceumrn.com.api.service;

import abraceumrn.com.api.domain.dto.TotalDTO;
import abraceumrn.com.api.domain.dto.ViewItems;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.items.RegisterItems;
import abraceumrn.com.api.domain.repository.ItemRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;


@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // validação
    private void validateItemDTO(ItemDTO dto) {
        if (dto.type() == ItemType.ROUPA) {
            if (dto.size() == null || dto.gender() == null) {
                throw new IllegalArgumentException("Clothes must contain size and gender.");
            }
        }
        if (dto.type() == ItemType.HIGIENE || dto.type() == ItemType.ALIMENTACAO) {
            if (dto.expirationAt() == null) {
                throw new IllegalArgumentException("Hygiene and food items must contain expiration date.");
            }
        }
        if (dto.quantity() < 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0 or 0.");
        }
    }


    // registrar item
    public RegisterItems registerItems(ItemDTO dto) {
        validateItemDTO(dto);
        if (dto.quantity() <=0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
        RegisterItems existing = findExistingItem(dto);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + dto.quantity());
            existing.setCreatedAt(LocalDate.now());
            return itemRepository.save(existing);
        }
        RegisterItems newItem = new RegisterItems(dto);
        if (newItem.getSize() != null) {
            newItem.setSize(dto.size().toUpperCase());
        }
        return itemRepository.save(newItem);
    }

    private RegisterItems findExistingItem(ItemDTO dto) {
        if (dto.type() == ItemType.HIGIENE || dto.type() == ItemType.ALIMENTACAO) {
            return itemRepository.findByTypeAndItemNameAndExpirationAt(dto.type(), dto.itemName(), dto.expirationAt());
        }
        return itemRepository.findByTypeAndItemNameAndSizeAndGender(dto.type(), dto.itemName(), dto.size(), dto.gender());
    }

    // Listagem total ou filtragem avançada
    public Page<ViewItems> listItems(ItemType type, String itemName, String size, Pageable pageable) {
        if (type != null && itemName != null && size != null) {
            return itemRepository.findByTypeAndItemNameAndSize(type, itemName, size, pageable);
        }
        if (type != null && itemName != null) {
            return itemRepository.findByTypeAndItemName(type, itemName, pageable);
        }
        if (type != null) {
            return itemRepository.findByType(type, pageable);
        }
        return itemRepository.findAll(pageable).map(ViewItems::new);
    }

    // Card total
    public TotalDTO totalOfItem() {
        Integer total = itemRepository.getQuantity();
        Integer totalTypes = itemRepository.getTotalTypes();
        Integer totalTypesDistinct = itemRepository.getTotalTypesDistintic();
        Integer totalUnique = itemRepository.getTotalUnique();
        if (total == null) total = 0;
        if (totalTypes == null) totalTypes = 0;
        if (totalTypesDistinct == null) totalTypesDistinct = 0;
        if (totalUnique == null) totalUnique = 0;
        return new TotalDTO(total, totalTypes, totalTypesDistinct, totalUnique);
    }

    // Delete de item
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    // update de item por id + atualização de item se já existir.
    public RegisterItems updateItemId(Long id, ItemDTO dto) {
        RegisterItems oldItem = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        validateItemDTO(dto);
        if (isSameItem(oldItem, dto)) {
            return updateQuantity(oldItem, dto.quantity());
        }
        return replaceItem(oldItem, dto);
    }

    private boolean isSameItem(RegisterItems item, ItemDTO dto) {
        return item.getType().equals(dto.type()) && item.getItemName().equals(dto.itemName()) && Objects.equals(item.getSize(), dto.size()) && Objects.equals(item.getGender(), dto.gender()) && Objects.equals(item.getExpirationAt(), dto.expirationAt());
    }

    private RegisterItems updateQuantity(RegisterItems item, int quantity) {
        item.setQuantity(quantity);
        item.setCreatedAt(LocalDate.now());
        return itemRepository.save(item);
    }

    private RegisterItems replaceItem(RegisterItems oldItem, ItemDTO dto) {
        RegisterItems existing = findExistingItem(dto);
        if (existing != null && !existing.getId().equals(oldItem.getId())) {
            existing.setQuantity(existing.getQuantity() + dto.quantity());
            existing.setCreatedAt(LocalDate.now());
            itemRepository.delete(oldItem);
            return itemRepository.save(existing);
        }
        itemRepository.delete(oldItem);
        RegisterItems newItem = new RegisterItems(dto);
        if (newItem.getSize() != null) {
            newItem.setSize(dto.size().toUpperCase());
        }
        return itemRepository.save(newItem);
    }
}
