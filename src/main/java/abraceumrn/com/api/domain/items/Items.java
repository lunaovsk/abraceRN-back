package abraceumrn.com.api.domain.items;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidade que representa um item no estoque.
 *
 * Armazena informações sobre um item incluindo nome, tipo, tamanho,
 * gênero, quantidade disponível e datas de criação e expiração.
 */
@Table(name = "item")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Items {

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

    /**
     * Construtor que cria um item a partir de um DTO.
     *
     * Inicializa o item com os dados fornecidos no DTO e define
     * a data de criação como a data atual.
     *
     * @param dto dados do item a ser criado
     */
    public Items(ItemDTO dto) {
        this.itemName = dto.itemName();
        this.type = dto.type();
        this.size = dto.size();
        this.quantity = dto.quantity();
        this.createdAt = LocalDate.now();
        this.expirationAt = dto.expirationAt();
        this.gender = dto.gender();
    }

}
