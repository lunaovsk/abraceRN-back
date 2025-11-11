package abraceumrn.com.api.domain.items;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "item")
@Entity(name = "Item")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Item {
	
	

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "item_name")
    private String itemName;
    
    @Enumerated(EnumType.STRING)
    private ItemType category;
    
    private String type; 
    
    @Column(name = "size")
    private String size;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Column(name = "expiration_at") 
    private LocalDate expiration_at;
    
    private Integer quantity;
    
    @Column(name = "create_at") 
    private LocalDateTime create_at;
    
    private Boolean status;

    public Item(ItemDTO dto) {
        this.itemName = dto.itemName();
        this.category = dto.category();
        this.type = dto.type(); 
        this.size = dto.size();
        this.gender = dto.gender();
        this.expiration_at = dto.expirationAt(); 
        this.quantity = dto.quantity();
        this.create_at = LocalDateTime.now(); 
        this.status = true;
    }

    public void putItem(ItemDTO itemDTO) {
        if (itemDTO.itemName() != null) this.itemName = itemDTO.itemName();
        if (itemDTO.category() != null) this.category = itemDTO.category();
        if (itemDTO.type() != null) this.type = itemDTO.type();
        if (itemDTO.size() != null) this.size = itemDTO.size();
        if (itemDTO.gender() != null) this.gender = itemDTO.gender();
        if (itemDTO.expirationAt() != null) this.expiration_at = itemDTO.expirationAt();
        if (itemDTO.quantity() != null) this.quantity = itemDTO.quantity();
    }

    public void deleteItem() {
        this.status = false;
    }
}