package abraceumrn.com.api.domain.items.strategy;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.domain.items.Items;
import abraceumrn.com.api.domain.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerishableItemMatcherTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private PerishableItemMatcher matcher;

    private Items item(ItemType type, String name, String size, Gender gender, LocalDate exp) {
        Items i = new Items();
        i.setType(type);
        i.setItemName(name);
        i.setSize(size);
        i.setGender(gender);
        i.setExpirationAt(exp);
        return i;
    }

    @Test
    @DisplayName("isSame deve ser falso quando o tamanho diferir, mesmo com tipo/nome/validade iguais")
    void isSameDiferenteTamanho() {
        LocalDate exp = LocalDate.of(2026, 12, 31);
        Items item = item(ItemType.HIGIENE, "Fralda", "P", Gender.M, exp);
        ItemDTO dto = new ItemDTO("Fralda", ItemType.HIGIENE, "G", 5, Gender.M, exp);

        assertThat(matcher.isSame(item, dto)).isFalse();
    }

    @Test
    @DisplayName("isSame deve ser falso quando o gênero diferir")
    void isSameDiferenteGenero() {
        LocalDate exp = LocalDate.of(2026, 12, 31);
        Items item = item(ItemType.HIGIENE, "Fralda", "P", Gender.M, exp);
        ItemDTO dto = new ItemDTO("Fralda", ItemType.HIGIENE, "P", 5, Gender.F, exp);

        assertThat(matcher.isSame(item, dto)).isFalse();
    }

    @Test
    @DisplayName("isSame deve ser verdadeiro quando tipo, nome, tamanho, gênero e validade forem iguais")
    void isSameTudoIgual() {
        LocalDate exp = LocalDate.of(2026, 12, 31);
        Items item = item(ItemType.HIGIENE, "Fralda", "P", Gender.M, exp);
        ItemDTO dto = new ItemDTO("Fralda", ItemType.HIGIENE, "P", 9, Gender.M, exp);

        assertThat(matcher.isSame(item, dto)).isTrue();
    }

    @Test
    @DisplayName("isSame deve ser verdadeiro quando tamanho e gênero forem nulos em ambos")
    void isSameCamposNulos() {
        LocalDate exp = LocalDate.of(2026, 12, 31);
        Items item = item(ItemType.HIGIENE, "Pomada", null, null, exp);
        ItemDTO dto = new ItemDTO("Pomada", ItemType.HIGIENE, null, 3, null, exp);

        assertThat(matcher.isSame(item, dto)).isTrue();
    }

    @Test
    @DisplayName("isSame deve ser falso quando a validade diferir")
    void isSameDiferenteValidade() {
        Items item = item(ItemType.HIGIENE, "Fralda", "P", Gender.M, LocalDate.of(2026, 12, 31));
        ItemDTO dto = new ItemDTO("Fralda", ItemType.HIGIENE, "P", 5, Gender.M, LocalDate.of(2027, 1, 1));

        assertThat(matcher.isSame(item, dto)).isFalse();
    }

    @Test
    @DisplayName("findExisting deve buscar incluindo tamanho e gênero, além de tipo/nome/validade")
    void findExistingIncluiTamanhoEGenero() {
        LocalDate exp = LocalDate.of(2026, 12, 31);
        ItemDTO dto = new ItemDTO("Fralda", ItemType.HIGIENE, "P", 5, Gender.M, exp);
        Items esperado = item(ItemType.HIGIENE, "Fralda", "P", Gender.M, exp);
        when(itemRepository.findByTypeAndItemNameAndSizeAndGenderAndExpirationAt(
                ItemType.HIGIENE, "Fralda", "P", Gender.M, exp)).thenReturn(esperado);

        Items result = matcher.findExisting(dto);

        assertThat(result).isSameAs(esperado);
        verify(itemRepository).findByTypeAndItemNameAndSizeAndGenderAndExpirationAt(
                ItemType.HIGIENE, "Fralda", "P", Gender.M, exp);
    }
}
