package abraceumrn.com.api.controller.kits;


import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import abraceumrn.com.api.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Kits", description = "Endpoints para retirada de kits no estoque")
@RestController
@RequestMapping("/kit")
public class KitsController {

    @Autowired
    private ItemService itemService;

    @PostMapping("/calcular")
    @Operation(summary = "Calcular kits possíveis", description = "Calcula quantos kits podem ser formados com os itens disponíveis e indica quais itens são limitantes para formar um novo kit.")
    @ApiResponse(responseCode = "200", description = "Cálculo realizado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos enviados para o cálculo.")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    public ResponseEntity<KitResponseDTO> calculateKits(@RequestBody @Valid RemoveKitDTO kitDTO) {
        KitResponseDTO result = itemService.totalKit(kitDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/gerar-kits")
    @Transactional
    @Operation(summary = "Gerar um kit e remover itens do estoque", description = "Gera um kit a partir dos itens informados e desconta automaticamente as quantidades correspondentes do estoque.")
    @ApiResponse(responseCode = "200", description = "Kit gerado e itens removidos do estoque com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos enviados para a montagem do kit.")
    @ApiResponse(responseCode = "404", description = "Algum dos itens informados não foi encontrado no estoque.")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    public ResponseEntity<Void> createKit(@RequestBody RemoveKitDTO kitDTO) {
        itemService.createdKit(kitDTO);
        return ResponseEntity.ok().build();
    }
}
