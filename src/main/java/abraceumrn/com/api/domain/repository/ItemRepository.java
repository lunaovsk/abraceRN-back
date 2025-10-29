package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.items.RegisterItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<RegisterItems, Long> {

    @Query("SELECT sum(i.quantity) AS totalItem FROM RegisterItems i")
    Integer getQuantity();
}
