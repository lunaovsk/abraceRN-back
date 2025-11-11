package abraceumrn.com.api.domain.enumItem;

public enum ItemType {

    ROUPA(1, "Roupa"),
    ACESSORIO(2, "Acessório"),
    HIGIENE(3, "Higiene"),
    ALIMENTACAO(4, "Alimentação"),
    INDEFINIDO(5, "Indefinido");

    private final int index;
    private final String desc;

    ItemType(int index, String desc) {
        this.index = index;
        this.desc = desc;

    }
    public String getDescricao() {
        return desc;
    }
    public int getIndice () { return index;}
    public static ItemType getDescricaoIndice(int index) {
        for (ItemType t : ItemType.values()) {
            if (t.index == index) {
                return t;
            }
        }
        return null;
    }



}
