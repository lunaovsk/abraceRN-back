package abraceumrn.com.api.domain.kit.strategy;

import org.springframework.stereotype.Service;

/**
 * Calculador de kits do tipo Enxoval.
 *
 * Usa o cálculo padrão de {@link AbstractKitCalculator}. Existe como ponto de
 * extensão para regras específicas de enxoval, caso venham a surgir.
 */
@Service
public class EnxovalKitCalculator extends AbstractKitCalculator {
}
