package abraceumrn.com.api.domain.enumItem;

public enum Gender {

    M("Masculino"),
    F("Feminino"),
    UNISSEX("Unissex");

    private final String description;
    private int index;

    Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
