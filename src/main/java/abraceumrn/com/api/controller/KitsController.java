package abraceumrn.com.api.controller;


import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import abraceumrn.com.api.service.ItemService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kit")
public class KitsController {

    @Autowired
    private ItemService itemService;


    //Calcular itens no estoque
    @GetMapping("/calcular")
    public ResponseEntity<KitResponseDTO> calculateKits(@RequestBody RemoveKitDTO kitDTO) {
        KitResponseDTO result = itemService.totalKit(kitDTO);
        return ResponseEntity.ok(result);
    }

    // Montar e retirar kit do estoque
    @PutMapping("/gerar-kits")
    @Transactional
    public ResponseEntity<String> createKit(@RequestBody RemoveKitDTO kitDTO) {
        itemService.createdKit(kitDTO);
        return ResponseEntity.ok().build();
    }
}
