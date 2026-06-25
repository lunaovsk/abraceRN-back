package abraceumrn.com.api.domain.items.strategy;

import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.items.Items;

/**
 * Estratégia que define como localizar, no estoque, um item já existente
 * equivalente ao informado em um {@link ItemDTO}.
 *
 * Cada tipo de item possui uma chave de unicidade diferente (ex.: itens
 * perecíveis usam data de validade; roupas usam tamanho e gênero).
 */
public interface ItemMatcher {

    /**
     * Busca um item existente equivalente ao DTO informado.
     *
     * @param dto dados do item a procurar
     * @return item encontrado ou {@code null} se não existir
     */
    Items findExisting(ItemDTO dto);

    /**
     * Verifica se um item já persistido tem a mesma identidade do DTO informado.
     *
     * Usado na atualização para decidir entre apenas alterar a quantidade
     * (mesma identidade) ou substituir o item (identidade diferente).
     *
     * @param item item já persistido
     * @param dto  dados informados na atualização
     * @return {@code true} se representam o mesmo item
     */
    boolean isSame(Items item, ItemDTO dto);
}