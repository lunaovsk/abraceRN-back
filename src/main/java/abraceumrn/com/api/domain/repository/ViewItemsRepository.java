package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.items.RegisterItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewItemsRepository extends JpaRepository<RegisterItems, Long>, JpaSpecificationExecutor<RegisterItems> {
}
