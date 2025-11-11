package abraceumrn.com.api.domain.repository;


import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.Item; 
import java.util.List;

// Imports do Spring
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {


    @Query("SELECT SUM(i.quantity) FROM Item i")
    Integer getSumOfAllQuantities(); 


    @Query("SELECT i FROM Item i WHERE " +
    	       "(:itemName IS NULL OR LOWER(i.itemName) LIKE LOWER(CONCAT('%', :itemName, '%'))) AND " +
    	       "(:category IS NULL OR i.category = :category) AND " + 
    	       "(:type IS NULL OR LOWER(i.itemName) = LOWER(:type)) AND " + 
    	       "(:size IS NULL OR :size = '' OR i.size = :size)") 
    	Page<Item> findWithFilters(
    	        @Param("itemName") String itemName,
    	        @Param("category") ItemType category, 
    	        @Param("type") String type, 
    	        @Param("size") String size,
    	        Pageable pageable);

    @Query("SELECT DISTINCT i.itemName FROM Item i WHERE i.category = :category ORDER BY i.itemName ASC")
    List<String> findDistinctTypesByCategory(@Param("category") ItemType category);

   
}
