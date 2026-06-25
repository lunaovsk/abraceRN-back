package abraceumrn.com.api.service;

import abraceumrn.com.api.domain.dto.KitDTO;
import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import abraceumrn.com.api.domain.items.Items;
import abraceumrn.com.api.domain.kit.validation.ValidationKit;
import abraceumrn.com.api.domain.kit.validation.ValidationKitQuantity;
import abraceumrn.com.api.domain.repository.ItemRepository;
import abraceumrn.com.api.domain.kit.strategy.KitFactory;
import abraceumrn.com.api.infra.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service que encapsula a lógica de negócio para gerenciamento de kits.
 *
 * Responsável por: calcular quantos kits podem ser formados com os itens
 * disponíveis e gerar kits, descontando as quantidades correspondentes do estoque.
 */
@Service
public class KitService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private KitFactory factory;
    @Autowired
    private List<ValidationKit> validations;
    @Autowired
    private ValidationKitQuantity quantityValidation;

    /**
     * Calcula a quantidade de kits possíveis com os itens disponíveis.
     *
     * Valida apenas o input (quantidade por kit) antes de calcular — não roda a
     * cadeia completa, pois o cálculo deve responder mesmo sem estoque suficiente.
     * Utiliza o padrão Strategy para delegar o cálculo conforme o tipo de kit.
     *
     * @param kit dados do kit com tipo e itens
     * @return resposta com quantidade de kits possíveis e itens limitantes
     */
    public KitResponseDTO totalKit(RemoveKitDTO kit) {
        quantityValidation.validation(kit);
        return factory.getCalculator(kit.type()).calcular(kit);
    }

    /**
     * Cria um kit removendo itens do estoque.
     *
     * Aplica todas as validações de kit antes de descontar as quantidades.
     * Se qualquer validação falhar, o estoque não é modificado.
     *
     * @param kit dados do kit a ser criado com itens e quantidades
     * @throws CustomException se alguma validação falhar
     */
    public void createdKit(RemoveKitDTO kit) {
        validations.forEach(v -> v.validation(kit));

        for (KitDTO k : kit.items()) {
            Items item = itemRepository.findByItemNameAndSizeAndGender(
                    k.itemName(), k.size(), k.gender());
            item.setQuantity(item.getQuantity() - k.quantity());
            itemRepository.save(item);
        }
    }
}