package abraceumrn.com.api.service;

import abraceumrn.com.api.domain.dto.TotalDTO;
import abraceumrn.com.api.domain.dto.ViewItems;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.items.RegisterItems;
import abraceumrn.com.api.domain.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;


@Service
public class ItemService {

    private ItemRepository itemRepository;


    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public RegisterItems registerItems(ItemDTO dto) {
        if (dto.type() == ItemType.ROUPA) {
            if (dto.size() == null || dto.gender() == null) {
                throw new IllegalArgumentException("Roupas precisam de tamanho e gênero.");
            }
        }
        if (dto.type() == ItemType.HIGIENE || dto.type() == ItemType.ALIMENTACAO) {
            if (dto.expirationAt() == null) {
                throw new IllegalArgumentException("Itens de higiene e alimentação precisam de data de validade.");
            }
        }
        if (dto.quantity() <= 0) {
            throw new IllegalArgumentException("Quantidade não pode ser menor ou igual a 0");
        }

        if (dto.type() == ItemType.HIGIENE || dto.type() == ItemType.ALIMENTACAO) {
            RegisterItems itemExistentExpirantion = itemRepository.findByTypeAndItemNameAndExpirationAt(
                    dto.type(), dto.itemName(), dto.expirationAt());

            if (itemExistentExpirantion != null) {
                itemExistentExpirantion.setQuantity(itemExistentExpirantion.getQuantity() + dto.quantity());
                itemExistentExpirantion.setCreatedAt(LocalDate.now());
                return itemRepository.save(itemExistentExpirantion);
            }
        }
        else {
            RegisterItems itemExistent = itemRepository.findByTypeAndItemNameAndSizeAndGender(
                    dto.type(), dto.itemName(), dto.size(), dto.gender());

            if (itemExistent != null) {
                itemExistent.setQuantity(itemExistent.getQuantity() + dto.quantity());
                itemExistent.setCreatedAt(LocalDate.now());
                return itemRepository.save(itemExistent);
            }
        }

        RegisterItems item = new RegisterItems(dto);
        if (item.getSize() != null) {
            item.setSize(dto.size().toUpperCase());
        }
        return itemRepository.save(item);
    }

    public Page<ViewItems> listItems(ItemType type, String itemName, String itemSize, Pageable pageable) {
        if (type != null && itemName != null && itemSize != null) {
            return itemRepository.findByTypeAndItemNameAndSize(type, itemName, itemSize, pageable);
        } else if (type != null && itemName != null) {
            return itemRepository.findByTypeAndItemName(type, itemName, pageable);
        } else if (type != null) {
            return itemRepository.findByType(type, pageable);
        }
        System.out.println(itemRepository.findAll(pageable).map(ViewItems::new));
        return itemRepository.findAll(pageable).map(ViewItems::new);
    }

    public Integer totalOfItem () {
        var total = itemRepository.getQuantity();
        var quantity = new TotalDTO(total).total();
        if (quantity == null) {
            return 0;
        }
        return quantity;
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public void updateItemId (Long id, ItemDTO itemDTO) {
        var i = itemRepository.findById(id);
        RegisterItems items = i.get();
        items.updateItem(itemDTO);
        itemRepository.save(items);
    }
}

