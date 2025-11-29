package abraceumrn.com.api.infra.exception;

import java.util.Map;

public class CustomException extends RuntimeException {
    private final String errorCode;
    private final Map<String, Object> details;

    public CustomException(String message) {
        super(message);
        this.errorCode = "ERRO_VALIDACAO";
        this.details = null;
    }

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    public CustomException(String message, String errorCode, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public static CustomException estoqueInsuficiente(String itemName, int disponivel, int necessario) {
        String message = String.format("Estoque insuficiente para %s. Disponível: %d, Necessário: %d",
                itemName, disponivel, necessario);
        return new CustomException(message, "ESTOQUE_INSUFICIENTE");
    }

    public static CustomException itemNaoEncontrado(String itemName) {
        return new CustomException("Item não encontrado: " + itemName, "ITEM_NAO_ENCONTRADO");
    }

    public static CustomException validacao(String message) {
        return new CustomException(message, "ERRO_VALIDACAO");
    }

    public static CustomException kitIncompleto(Map<String, Integer> itensFaltantes) {
        return new CustomException(
                "Não é possível montar o kit completo",
                "KIT_INCOMPLETO",
                Map.of("itensFaltantes", itensFaltantes)
        );
    }
}