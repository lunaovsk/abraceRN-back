package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.items.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewItemsRepository extends JpaRepository<Items, Long>, JpaSpecificationExecutor<Items> {
}
