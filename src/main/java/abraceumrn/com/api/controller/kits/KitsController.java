package abraceumrn.com.api.controller.kits;


import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import abraceumrn.com.api.domain.items.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para gerenciamento de kits.
 *
 * Responsável por: calcular quantos kits podem ser formados com os itens
 * disponíveis e gerenciar a geração de kits (remoção de itens do estoque).
 * Todos os endpoints requerem autenticação JWT.
 */
@Tag(name = "Kits", description = "Endpoints para retirada de kits no estoque")
@RestController
@RequestMapping("/kit")
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("isAuthenticated()")
public class KitsController {

    @Autowired
    private ItemService itemService;

    /**
     * Calcula quantos kits podem ser formados com os itens disponíveis.
     *
     * Retorna a quantidade máxima de kits possíveis e identifica quais itens
     * são limitantes (têm quantidade insuficiente).
     *
     * @param kitDTO dados do kit com tipo e itens necessários
     * @return resposta com quantidade de kits possíveis e itens limitantes
     */
    @PostMapping("/calcular")
    @Operation(summary = "Calcular kits possíveis", description = "Calcula quantos kits podem ser formados com os itens disponíveis e indica quais itens são limitantes para formar um novo kit.")
    @ApiResponse(responseCode = "200", description = "Cálculo realizado com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos enviados para o cálculo.")
    @ApiResponse(responseCode = "401", description = "Usuário não autenticado.")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
    public ResponseEntity<KitResponseDTO> calculateKits(@RequestBody @Valid RemoveKitDTO kitDTO) {
        KitResponseDTO result = itemService.totalKit(kitDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Cria um kit e remove os itens correspondentes do estoque.
     *
     * Valida se há quantidade suficiente de cada item antes de proceder.
     * Se não houver, lança exceção sem modificar o estoque.
     *
     * @param kitDTO dados do kit com itens e quantidades a remover
     * @return status 200 OK se o kit foi criado com sucesso
     * @throws CustomException se não houver quantidade suficiente ou item não existir
     */
    @PutMapping("/gerar-kits")
    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
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
