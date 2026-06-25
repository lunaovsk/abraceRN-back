package abraceumrn.com.api.domain.kit.validation;

import abraceumrn.com.api.domain.dto.RemoveKitDTO;

/**
 * Estratégia de validação para a geração de kits.
 *
 * Cada implementação representa uma regra isolada que deve ser satisfeita
 * antes de um kit ser gerado e ter os itens descontados do estoque.
 */
public interface ValidationKit {

    void validation(RemoveKitDTO dto);

}
