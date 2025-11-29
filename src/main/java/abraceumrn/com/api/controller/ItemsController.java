package abraceumrn.com.api.controller;


import abraceumrn.com.api.domain.dto.TotalDTO;
import abraceumrn.com.api.domain.dto.ViewItems;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.ItemDTO;
import abraceumrn.com.api.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Itens", description = "Endpoints para gerenciamento de itens no estoque")
@RestController
@RequestMapping("/dashboard")
public class ItemsController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar um item no estoque", description = "Cria um novo item no estoque com as informações fornecidas.")
    @ApiResponse(responseCode = "201", description = "Item cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> register(@RequestBody @Valid ItemDTO dto) {
        itemService.registerItems(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all-items")
    @Operation(summary = "Listar itens", description = "Retorna uma lista paginada de itens filtrando por nome, tamanho, categoria ou gênero.")
    @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Page<ViewItems>> getAllItems(
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) String itemSize,
            @RequestParam(required = false) ItemType category,
            @RequestParam(required = false) Gender gender,
            Pageable pageable) {

        Page<ViewItems> items = itemService.listItems(itemName, itemSize, category, gender, pageable);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/total")
    @Operation(summary = "Obter o total de itens", description = "Retorna a quantidade total de itens cadastrados no estoque, a quantidade de itens únicos e tipos de itens cadastrados.")
    @ApiResponse(responseCode = "200", description = "Total retornado com sucesso")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<TotalDTO> totalItem() {
        var item = itemService.totalOfItem();
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/deletar/{id}")
    @Transactional
    @Operation(summary = "Deletar um item", description = "Remove um item do estoque pelo ID.")
    @ApiResponse(responseCode = "200", description = "Item deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item não encontrado para exclusão")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/atualizar/{id}")
    @Transactional
    @Operation(summary = "Atualizar um item do estoque", description = "Atualiza as informações de um item existente no estoque utilizando seu ID.")
    @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    public ResponseEntity<Void> updateItem(@PathVariable Long id, @RequestBody @Valid ItemDTO itemDTO) {
        itemService.updateItemId(id, itemDTO);
        return ResponseEntity.ok().build();
    }
}
