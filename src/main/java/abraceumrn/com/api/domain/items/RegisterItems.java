package abraceumrn.com.api.domain.items;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import jakarta.persistence.*;

import java.time.LocalDate;

@Table(name = "item")
@Entity
public class RegisterItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String itemName;
    @Enumerated(EnumType.STRING)
    private ItemType type;
    private String size;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int quantity;
    private LocalDate createdAt;
    private LocalDate expirationAt;

    public RegisterItems() {

    }
    public RegisterItems(ItemDTO dto) {
        this.itemName = dto.itemName();
        this.type = dto.type();
        this.size = dto.size();
        this.quantity = dto.quantity();
        this.createdAt = LocalDate.now();
        this.expirationAt = dto.expirationAt();
        this.gender = dto.gender();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getExpirationAt() {
        return expirationAt;
    }

    public void setExpirationAt(LocalDate expirationAt) {
        this.expirationAt = expirationAt;
    }
}
