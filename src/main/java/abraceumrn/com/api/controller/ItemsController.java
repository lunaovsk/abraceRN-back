package abraceumrn.com.api.controller;


import abraceumrn.com.api.domain.dto.TotalDTO;
import abraceumrn.com.api.domain.dto.ViewItems;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.service.ItemService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
public class ItemsController {

    @Autowired
    private ItemService itemService;


    @PostMapping
    @Transactional
    public ResponseEntity register (@RequestBody @Valid ItemDTO dto) {
        itemService.registerItems(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("/all-items")
    public ResponseEntity<Page<ViewItems>> getAllItems(
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String itemSize,
            @RequestParam(required = false) ItemType category,
            @RequestParam(required = false) Gender gender,
            Pageable pageable) {

        Page<ViewItems> items = itemService.listItems(itemName, itemSize, category,gender, pageable);
        return ResponseEntity.ok(items);
    }


    @GetMapping("/total")
    public ResponseEntity totalItem () {
        var item = itemService.totalOfItem();
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/deletar/{id}")
    @Transactional
    public ResponseEntity deleteItem (@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/atualizar/{id}")
    @Transactional
    public ResponseEntity putItem (@PathVariable Long id, @RequestBody @Valid ItemDTO itemDTO) {
        itemService.updateItemId(id, itemDTO);
        return ResponseEntity.ok().build();
    }


}
