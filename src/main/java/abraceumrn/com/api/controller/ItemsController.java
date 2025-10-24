package abraceumrn.com.api.controller;


import abraceumrn.com.api.domain.dto.TotalDTO;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.service.ItemService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping
    public ResponseEntity totalItem () {
        var total = itemService.totalOfItem();
        var item = new TotalDTO(total);
        return ResponseEntity.ok(item);
    }
}
