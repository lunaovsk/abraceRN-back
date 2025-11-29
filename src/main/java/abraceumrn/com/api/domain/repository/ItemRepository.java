package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.dto.ViewItems;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.RegisterItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;

@Repository
public interface ItemRepository extends JpaRepository<RegisterItems, Long> {


    @Query("SELECT sum(i.quantity) AS totalItem FROM RegisterItems i")
    Integer getQuantity();
    RegisterItems findByTypeAndItemNameAndSizeAndGender (ItemType itemType, String itemName, String itemSize, Gender gender);
    RegisterItems findByTypeAndItemNameAndExpirationAt (ItemType itemType, String itemName, LocalDate expiratedAt);


    @Query("SELECT COUNT(DISTINCT i.itemName) as totalTypes FROM RegisterItems i")
    Integer getTotalTypes();

    @Query("SELECT COUNT(DISTINCT i.type) as totalTypes FROM RegisterItems i")
    Integer getTotalTypesDistintic();

    @Query("SELECT COUNT(i.quantity) AS totalItemsUnique FROM RegisterItems i WHERE i.quantity = 1")
    Integer getTotalUnique();

    RegisterItems findByItemNameAndSizeAndGender(String itemName, String itemSize, Gender gender);

    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM RegisterItems i WHERE " + "i.itemName = :itemName AND i.gender = :gender AND " + "(:size IS NULL AND i.size IS NULL OR i.size = :size)")
    Integer getTotalForItem(String itemName, String size, Gender gender);

    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM RegisterItems i WHERE " + "i.itemName = :itemName AND i.size = :size OR i.gender IS NULL")
    Integer getTotalForItemWithoutSizeAndGender(String itemName, String size);


}
