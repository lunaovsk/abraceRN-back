package abraceumrn.com.api.domain.strategy;

import abraceumrn.com.api.domain.dto.KitDTO;
import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import abraceumrn.com.api.infra.exception.CustomException;
import abraceumrn.com.api.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractKitCalculator implements KitCalcular {

    @Autowired
    protected ItemRepository itemRepository;

    @Override
    public KitResponseDTO calcularKit(RemoveKitDTO kit) {
        Map<String, Integer> kitsPorItem = new HashMap<>();
        Map<String, Integer> faltantes = new HashMap<>();

        for (KitDTO item : kit.items()) {
            Integer totalEstoque;
            if ((item.size() == null || item.size().isEmpty()) && item.gender() == null) {
                totalEstoque = itemRepository.getTotalForItemWithoutSizeAndGender(item.itemName(), item.size());
            } else {
                totalEstoque = itemRepository.getTotalForItem(item.itemName(), item.size(), item.gender());
            }

            if (totalEstoque == null) totalEstoque = 0;

            Integer quantidadePorKit = item.quantity();
            if (quantidadePorKit <= 0) {
                throw CustomException.validacao("Quantidade por kit inválida para: " + item.itemName());
            }

            Integer kitsPossiveis = totalEstoque / quantidadePorKit;
            kitsPorItem.put(item.itemName(), kitsPossiveis);

            Integer falta = (kitsPossiveis == 0) ?
                    quantidadePorKit - totalEstoque :
                    quantidadePorKit - (totalEstoque % quantidadePorKit);

            if (falta.equals(quantidadePorKit)) falta = 0;

            if (kitsPossiveis == 0 || falta > 0) {
                faltantes.put(item.itemName(), falta);
            }
        }

        Integer totalKits = kitsPorItem.values()
                .stream()
                .min(Integer::compareTo)
                .orElse(0);

        Map<String, Integer> itensLimitantes = kitsPorItem.entrySet().stream()
                .filter(e -> e.getValue().equals(totalKits))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> faltantes.getOrDefault(e.getKey(), 0)
                ));

        return new KitResponseDTO(totalKits, itensLimitantes);
    }
}