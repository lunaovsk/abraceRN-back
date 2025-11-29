package abraceumrn.com.api.service;

import abraceumrn.com.api.domain.dto.*;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.infra.exception.CustomException;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.items.RegisterItems;
import abraceumrn.com.api.domain.repository.ItemRepository;
import abraceumrn.com.api.domain.repository.ViewItemsRepository;
import abraceumrn.com.api.domain.strategy.KitCalcular;
import abraceumrn.com.api.domain.strategy.KitFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import jakarta.persistence.criteria.Predicate;
import java.util.*;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final ViewItemsRepository viewItemsRepository;
    private final KitFactory factory;

    @Autowired
    public ItemService(ItemRepository itemRepository, ViewItemsRepository viewItemsRepository, KitFactory factory) {
        this.itemRepository = itemRepository;
        this.viewItemsRepository = viewItemsRepository;
        this.factory = factory;
    }

    // Validação
    private void validateItemDTO(ItemDTO dto) {
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
        if (dto.quantity() < 0) {
            throw CustomException.validacao("Quantidade deve ser maior ou igual a 0.");
        }
    }

    // Registrar item
    public RegisterItems registerItems(ItemDTO dto) {
        validateItemDTO(dto);
        if (dto.quantity() <= 0) {
            throw CustomException.validacao("Quantidade deve ser maior que 0.");
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

    // Listagem
    public Page<ViewItems> listItems(String itemName, String itemSize, ItemType category, Gender gender, Pageable pageable) {
        Page<RegisterItems> page = viewItemsRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) predicates.add(cb.equal(root.get("type"), category));
            if (itemName != null && !itemName.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("itemName")), "%" + itemName.toLowerCase() + "%"));
            }
            if (gender != null) predicates.add(cb.equal(root.get("gender"), gender));
            if (itemSize != null && !itemSize.isEmpty()) predicates.add(cb.equal(root.get("size"), itemSize));

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        return page.map(ViewItems::new);
    }

    // Card total
    public TotalDTO totalOfItem() {
        Integer total = itemRepository.getQuantity() != null ? itemRepository.getQuantity() : 0;
        Integer totalTypes = itemRepository.getTotalTypes() != null ? itemRepository.getTotalTypes() : 0;
        Integer totalTypesDistinct = itemRepository.getTotalTypesDistintic() != null ? itemRepository.getTotalTypesDistintic() : 0;
        Integer totalUnique = itemRepository.getTotalUnique() != null ? itemRepository.getTotalUnique() : 0;

        return new TotalDTO(total, totalTypes, totalTypesDistinct, totalUnique);
    }

    // Delete de item
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw CustomException.itemNaoEncontrado("ID " + id);
        }
        itemRepository.deleteById(id);
    }

    // Update de item
    public RegisterItems updateItemId(Long id, ItemDTO dto) {
        RegisterItems oldItem = itemRepository.findById(id)
                .orElseThrow(() -> CustomException.itemNaoEncontrado("Item não encontrado"));
        validateItemDTO(dto);

        if (isSameItem(oldItem, dto)) {
            return updateQuantity(oldItem, dto.quantity());
        }
        return replaceItem(oldItem, dto);
    }

    private boolean isSameItem(RegisterItems item, ItemDTO dto) {
        return item.getType().equals(dto.type()) &&
                item.getItemName().equals(dto.itemName()) &&
                Objects.equals(item.getSize(), dto.size()) &&
                Objects.equals(item.getGender(), dto.gender()) &&
                Objects.equals(item.getExpirationAt(), dto.expirationAt());
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

    // KITS
    public void createdKit(RemoveKitDTO kit) {
        KitResponseDTO calculo = totalKit(kit);

        if (calculo.kitsPossible() <= 0) {
            throw CustomException.kitIncompleto(calculo.limitingItems());
        }

        for (KitDTO k : kit.items()) {
            RegisterItems item = itemRepository.findByItemNameAndSizeAndGender(
                    k.itemName(), k.size(), k.gender());

            if (item == null) {
                throw CustomException.itemNaoEncontrado(
                        k.itemName() + " - Tamanho: " + k.size() + " - Gênero: " + k.gender()
                );
            }

            if (item.getQuantity() < k.quantity()) {
                throw CustomException.estoqueInsuficiente(
                        k.itemName(), item.getQuantity(), k.quantity()
                );
            }

            item.setQuantity(item.getQuantity() - k.quantity());
            itemRepository.save(item);
        }
    }

    // Cálculo de kits
    public KitResponseDTO totalKit(RemoveKitDTO kit) {
        KitCalcular strategy = factory.getCalculator(kit.type());
        return strategy.calcular(kit);
    }
}