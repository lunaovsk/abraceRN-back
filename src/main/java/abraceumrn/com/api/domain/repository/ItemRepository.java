package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT SUM(i.quantity) FROM Item i")
    Integer getSumOfAllQuantities();

    @Query("SELECT i FROM Item i WHERE " +
           "(:itemName IS NULL OR i.itemName LIKE %:itemName%) AND " +
           "(:category IS NULL OR i.category = :category) AND " +
           "(:type IS NULL OR i.type = :type) AND " +
           "(:size IS NULL OR i.size = :size)")
    Page<Item> findWithFilters(
            @Param("itemName") String itemName,
            @Param("category") ItemType category,
            @Param("type") String type,
            @Param("size") String size,
            Pageable pageable);

    @Query("SELECT DISTINCT i.itemName FROM Item i WHERE i.category = :category ORDER BY i.itemName ASC")
    List<String> findDistinctTypesByCategory(@Param("category") ItemType category);

    @Query("SELECT DISTINCT i.itemName FROM Item i WHERE i.itemName IS NOT NULL AND i.itemName != '' ORDER BY i.itemName ASC")
    List<String> findAllDistinctItemNames();
}