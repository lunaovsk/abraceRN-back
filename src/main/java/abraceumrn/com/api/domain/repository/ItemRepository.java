package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.dto.ViewItems;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.RegisterItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<RegisterItems, Long> {

    @Query("SELECT sum(i.quantity) AS totalItem FROM RegisterItems i")
    Integer getQuantity();

    Page<ViewItems> findByType (ItemType itemType, Pageable pageable);
    Page<ViewItems> findByItemName (String itemName, Pageable pageable);
    Page<ViewItems> findByTypeAndItemName (ItemType itemType, String itemName, Pageable pageable);
    Page<ViewItems> findByTypeAndItemNameAndSize (ItemType itemType, String itemName, String itemSize, Pageable pageable);





}
