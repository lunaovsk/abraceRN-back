package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.Items;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager em;

    private Items createItem(ItemType type, String name, String size, Gender gender, LocalDate expiration, int quantity) {
        Items item = new Items();
        item.setItemName(name);
        item.setType(type);
        item.setSize(size);
        item.setGender(gender);
        item.setQuantity(quantity);
        item.setCreatedAt(LocalDate.now());
        item.setExpirationAt(expiration);
        return em.persist(item);
    }

    @Test
    @DisplayName("Deve encontrar item por tipo, nome, tamanho e gênero (roupas/enxoval)")
    void findByTypeAndItemNameAndSizeAndGender() {
        createItem(ItemType.ROUPA, "Body", "P", Gender.M, null, 10);
        em.flush();
        em.clear();

        Items result = itemRepository.findByTypeAndItemNameAndSizeAndGender(
                ItemType.ROUPA, "Body", "P", Gender.M);

        assertThat(result).isNotNull();
        assertThat(result.getItemName()).isEqualTo("Body");
        assertThat(result.getSize()).isEqualTo("P");
        assertThat(result.getGender()).isEqualTo(Gender.M);
    }

    @Test
    @DisplayName("Deve encontrar item por tipo, nome e data de validade (higiene/alimentação)")
    void findByTypeAndItemNameAndExpirationAt() {
        LocalDate expiration = LocalDate.of(2026, 12, 31);
        createItem(ItemType.HIGIENE, "Fralda", null, null, expiration, 5);
        em.flush();
        em.clear();

        Items result = itemRepository.findByTypeAndItemNameAndExpirationAt(
                ItemType.HIGIENE, "Fralda", expiration);

        assertThat(result).isNotNull();
        assertThat(result.getItemName()).isEqualTo("Fralda");
        assertThat(result.getExpirationAt()).isEqualTo(expiration);
    }

    @Test
    @DisplayName("Deve encontrar item por nome, tamanho e gênero para montagem de kit")
    void findByItemNameAndSizeAndGender() {
        createItem(ItemType.ROUPA, "Macacão", "M", Gender.F, null, 8);
        em.flush();
        em.clear();

        Items result = itemRepository.findByItemNameAndSizeAndGender(
                "Macacão", "M", Gender.F);

        assertThat(result).isNotNull();
        assertThat(result.getItemName()).isEqualTo("Macacão");
        assertThat(result.getSize()).isEqualTo("M");
        assertThat(result.getGender()).isEqualTo(Gender.F);
    }

    @Test
    @DisplayName("Deve retornar total de itens com tamanho e gênero para cálculo de kit")
    void getTotalForItem() {
        createItem(ItemType.ROUPA, "Calça", "G", Gender.M, null, 6);
        createItem(ItemType.ROUPA, "Calça", "G", Gender.M, null, 4);
        em.flush();
        em.clear();

        Integer total = itemRepository.getTotalForItem("Calça", "G", Gender.M);

        assertThat(total).isEqualTo(10);
    }

    @Test
    @DisplayName("Deve retornar total de itens sem tamanho e gênero para cálculo de kit (higiene/alimentação)")
    void getTotalForItemWithoutSizeAndGender() {
        LocalDate expiration = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        createItem(ItemType.HIGIENE, "Pomada", null, null, expiration, 3);
        createItem(ItemType.HIGIENE, "Pomada", null, null, expiration, 7);
        em.flush();
        em.clear();

        Integer total = itemRepository.getTotalForItemWithoutSizeAndGender("Pomada", null);

        assertThat(total).isEqualTo(10);
    }
}