package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface ItemRepository extends JpaRepository<Items, Long> {


    @Query("SELECT sum(i.quantity) AS totalItem FROM Items  i")
    Integer getQuantity();
    Items findByTypeAndItemNameAndSizeAndGender (ItemType itemType, String itemName, String itemSize, Gender gender);
    Items findByTypeAndItemNameAndExpirationAt (ItemType itemType, String itemName, LocalDate expiratedAt);

    @Query("SELECT COUNT(DISTINCT i.itemName) as totalTypes FROM Items  i")
    Integer getTotalTypes();

    @Query("SELECT COUNT(DISTINCT i.type) as totalTypes FROM Items  i")
    Integer getTotalTypesDistintic();

    @Query("SELECT COUNT(i.quantity) AS totalItemsUnique FROM Items  i WHERE i.quantity = 1")
    Integer getTotalUnique();

    Items findByItemNameAndSizeAndGender(String itemName, String itemSize, Gender gender);

    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM Items  i WHERE " + "i.itemName = :itemName AND i.gender = :gender AND " + "(:size IS NULL AND i.size IS NULL OR i.size = :size)")
    Integer getTotalForItem(String itemName, String size, Gender gender);

    @Query("SELECT COALESCE(SUM(i.quantity), 0) FROM Items  i WHERE " + "i.itemName = :itemName AND i.size = :size OR i.gender IS NULL")
    Integer getTotalForItemWithoutSizeAndGender(String itemName, String size);


}
