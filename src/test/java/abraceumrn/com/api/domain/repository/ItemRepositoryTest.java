package abraceumrn.com.api.domain.repository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ItemRepositoryTest {



    @Test
    void findByTypeAndItemNameAndSizeAndGender() {
    }

    @Test
    void findByTypeAndItemNameAndExpirationAt() {
    }

    @Test
    void findByItemNameAndSizeAndGender() {
    }

    @Test
    void getTotalForItem() {
    }

    @Test
    void getTotalForItemWithoutSizeAndGender() {
    }
}