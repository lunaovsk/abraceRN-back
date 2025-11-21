package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.dto.ViewItems;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.RegisterItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ItemRepository extends JpaRepository<RegisterItems, Long> {


    @Query("SELECT sum(i.quantity) AS totalItem FROM RegisterItems i")
    Integer getQuantity();
    Page<ViewItems> findByType (ItemType itemType, Pageable pageable);
    Page<ViewItems> findByItemName (String itemName, Pageable pageable);
    Page<ViewItems> findByTypeAndItemName (ItemType itemType, String itemName, Pageable pageable);
    Page<ViewItems> findByTypeAndItemNameAndSize (ItemType itemType, String itemName, String itemSize, Pageable pageable);
    RegisterItems findByTypeAndItemNameAndSizeAndGender (ItemType itemType, String itemName, String itemSize, Gender gender);
    RegisterItems findByTypeAndItemNameAndExpirationAt (ItemType itemType, String itemName, LocalDate expiratedAt);


    @Query("SELECT COUNT(DISTINCT i.itemName) as totalTypes FROM RegisterItems i")
    Integer getTotalTypes();

    @Query("SELECT COUNT(DISTINCT i.type) as totalTypes FROM RegisterItems i")
    Integer getTotalTypesDistintic();

    @Query("SELECT COUNT(i.quantity) AS totalItemsUnique FROM RegisterItems i WHERE i.quantity = 1")
    Integer getTotalUnique();


}
