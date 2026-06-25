package abraceumrn.com.api.domain.kit.strategy;

import org.springframework.stereotype.Service;

/**
 * Calculador de kits do tipo Higiene.
 *
 * Usa o cálculo padrão de {@link AbstractKitCalculator}. Existe como ponto de
 * extensão para regras específicas de higiene (ex.: validade), caso venham a surgir.
 */
@Service
public class HigieneKitCalculator extends AbstractKitCalculator {
}
