package abraceumrn.com.api.service;

import abraceumrn.com.api.domain.dto.TotalDTO;
import abraceumrn.com.api.domain.dto.ViewItems;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.items.RegisterItems;
import abraceumrn.com.api.domain.repository.ItemRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.*;
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

        RegisterItems item = new RegisterItems(dto);
        if (item.getSize() != null) {
            item.setSize(dto.size().toUpperCase());
        }
        return itemRepository.save(item);
    }


    public Page<ViewItems> listItems (Pageable pageable) {
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
}

