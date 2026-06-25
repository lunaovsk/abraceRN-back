package abraceumrn.com.api.service;

import abraceumrn.com.api.domain.dto.*;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.items.Items;
import abraceumrn.com.api.infra.exception.CustomException;
import abraceumrn.com.api.domain.items.strategy.ItemMatcherFactory;
import abraceumrn.com.api.domain.items.validation.ValidationItems;
import abraceumrn.com.api.domain.repository.ItemRepository;
import abraceumrn.com.api.domain.repository.ViewItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import jakarta.persistence.criteria.Predicate;
import java.util.*;

/**
 * Service que encapsula a lógica de negócio para gerenciamento de itens.
 *
 * Responsável por: validar, criar, atualizar, deletar e listar itens do estoque.
 * Também gerencia a criação de kits a partir dos itens disponíveis.
 */
@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ViewItemsRepository viewItemsRepository;
    @Autowired
    private ItemMatcherFactory matcherFactory;
    @Autowired
    private List<ValidationItems> validations;

    /**
     * Valida um DTO de item usando as estratégias de validação injetadas.
     *
     * @param dto dados a serem validados
     * @throws CustomException se alguma validação falhar
     */
    private void validate(ItemDTO dto) {
        validations.forEach(v -> v.validation(dto));
    }

    /**
     * Registra um novo item no estoque.
     *
     * Se um item com as mesmas características já existe, incrementa a quantidade.
     * Caso contrário, cria um novo registro.
     *
     * @param dto dados do item a ser registrado
     * @return item persistido
     * @throws CustomException se a quantidade for inválida ou validação falhar
     */
    public Items registerItems(ItemDTO dto) {
        validate(dto);

        Items existing = findExistingItem(dto);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + dto.quantity());
            existing.setCreatedAt(LocalDate.now());
            return itemRepository.save(existing);
        }

        Items newItem = new Items(dto);
        if (newItem.getSize() != null) {
            newItem.setSize(dto.size().toUpperCase());
        }
        return itemRepository.save(newItem);
    }

    /**
     * Busca um item existente no banco que match com os dados do DTO.
     *
     * Delega para o {@link ItemMatcherFactory}, que escolhe a estratégia de
     * busca conforme o tipo do item (perecível x não perecível).
     *
     * @param dto dados do item a buscar
     * @return item encontrado ou null se não existir
     */
    private Items findExistingItem(ItemDTO dto) {
        return matcherFactory.getMatcher(dto.type()).findExisting(dto);
    }

    /**
     * Lista itens com filtros opcionais e paginação.
     *
     * Utiliza Criteria API para construir queries dinâmicas.
     *
     * @param itemName filtro por nome (case-insensitive)
     * @param itemSize filtro por tamanho
     * @param category filtro por categoria (tipo)
     * @param gender filtro por gênero
     * @param pageable informações de paginação
     * @return página de itens encontrados
     */
    public Page<ViewItems> listItems(String itemName, String itemSize, ItemType category, Gender gender, Pageable pageable) {
        Page<Items> page = viewItemsRepository.findAll((root, query, cb) -> {
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

    /**
     * Obtém estatísticas totais do estoque.
     *
     * @return DTO com quantidade total, tipos únicos e itens únicos
     */
    public TotalDTO totalOfItem() {
        Integer total = itemRepository.getQuantity() != null ? itemRepository.getQuantity() : 0;
        Integer totalTypes = itemRepository.getTotalTypes() != null ? itemRepository.getTotalTypes() : 0;
        Integer totalTypesDistinct = itemRepository.getTotalTypesDistintic() != null ? itemRepository.getTotalTypesDistintic() : 0;
        Integer totalUnique = itemRepository.getTotalUnique() != null ? itemRepository.getTotalUnique() : 0;

        return new TotalDTO(total, totalTypes, totalTypesDistinct, totalUnique);
    }

    /**
     * Deleta um item do estoque.
     *
     * @param id identificador do item a deletar
     * @throws CustomException se o item não existir
     */
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw CustomException.itemNaoEncontrado("ID " + id);
        }
        itemRepository.deleteById(id);
    }

    /**
     * Atualiza um item existente.
     *
     * Se as características principais forem iguais, apenas a quantidade é atualizada.
     * Caso contrário, substitui o item antigo por um novo.
     *
     * @param id identificador do item a atualizar
     * @param dto novos dados do item
     * @return item atualizado
     * @throws CustomException se o item não existir ou validação falhar
     */
    public Items updateItemId(Long id, ItemDTO dto) {
        Items oldItem = itemRepository.findById(id)
                .orElseThrow(() -> CustomException.itemNaoEncontrado("Item não encontrado"));
        validate(dto);

        if (matcherFactory.getMatcher(dto.type()).isSame(oldItem, dto)) {
            return updateQuantity(oldItem, dto.quantity());
        }
        return replaceItem(oldItem, dto);
    }

    /**
     * Atualiza apenas a quantidade de um item.
     *
     * @param item item a atualizar
     * @param quantity nova quantidade
     * @return item persistido
     */
    private Items updateQuantity(Items item, int quantity) {
        item.setQuantity(quantity);
        item.setCreatedAt(LocalDate.now());
        return itemRepository.save(item);
    }

    /**
     * Substitui um item antigo por um novo.
     *
     * Se um item com as mesmas características já existe, incrementa sua quantidade.
     * Caso contrário, cria um novo item e deleta o antigo.
     *
     * @param oldItem item a ser substituído
     * @param dto dados do novo item
     * @return item persistido
     */
    private Items replaceItem(Items oldItem, ItemDTO dto) {
        Items existing = findExistingItem(dto);
        if (existing != null && !existing.getId().equals(oldItem.getId())) {
            existing.setQuantity(existing.getQuantity() + dto.quantity());
            existing.setCreatedAt(LocalDate.now());
            itemRepository.delete(oldItem);
            return itemRepository.save(existing);
        }
        itemRepository.delete(oldItem);
        Items newItem = new Items(dto);
        if (newItem.getSize() != null) {
            newItem.setSize(dto.size().toUpperCase());
        }
        return itemRepository.save(newItem);
    }

}