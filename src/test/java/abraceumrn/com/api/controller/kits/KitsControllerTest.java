package abraceumrn.com.api.controller.kits;

import abraceumrn.com.api.domain.dto.KitDTO;
import abraceumrn.com.api.domain.dto.KitResponseDTO;
import abraceumrn.com.api.domain.dto.RemoveKitDTO;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.KitType;
import abraceumrn.com.api.domain.items.ItemService;
import abraceumrn.com.api.infra.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class KitsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<RemoveKitDTO> removeKitDTOJacksonTester;

    @MockitoBean
    private ItemService itemService;

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 200 com resultado ao calcular kits de enxoval")
    void calculateKitsEnxoval() throws Exception {
        var kit = new RemoveKitDTO(KitType.ENXOVAL, List.of(
                new KitDTO("Body", Gender.M, "P", 2)
        ));

        when(itemService.totalKit(any())).thenReturn(new KitResponseDTO(5, Map.of()));

        var response = mvc.perform(post("/kit/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(removeKitDTOJacksonTester.write(kit).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 200 com resultado ao calcular kits de higiene")
    void calculateKitsHigiene() throws Exception {
        var kit = new RemoveKitDTO(KitType.HIGIENE, List.of(
                new KitDTO("Fralda", null, null, 3)
        ));

        when(itemService.totalKit(any())).thenReturn(new KitResponseDTO(3, Map.of()));

        var response = mvc.perform(post("/kit/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(removeKitDTOJacksonTester.write(kit).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando quantidade do item for inválida no cálculo")
    void calculateKitsQuantidadeInvalida() throws Exception {
        doThrow(CustomException.validacao("Quantidade por kit inválida para: Body"))
                .when(itemService).totalKit(any());

        var kit = new RemoveKitDTO(KitType.ENXOVAL, List.of(
                new KitDTO("Body", Gender.M, "P", 0)
        ));

        var response = mvc.perform(post("/kit/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(removeKitDTOJacksonTester.write(kit).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando body estiver malformado no cálculo")
    void calculateKitsBodyMalformado() throws Exception {
        var response = mvc.perform(post("/kit/calcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ malformado }"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 200 ao gerar kit de enxoval com sucesso")
    void createKitEnxoval() throws Exception {
        var kit = new RemoveKitDTO(KitType.ENXOVAL, List.of(
                new KitDTO("Body", Gender.M, "P", 2)
        ));

        var response = mvc.perform(put("/kit/gerar-kits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(removeKitDTOJacksonTester.write(kit).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 200 ao gerar kit de higiene com sucesso")
    void createKitHigiene() throws Exception {
        var kit = new RemoveKitDTO(KitType.HIGIENE, List.of(
                new KitDTO("Fralda", null, null, 3)
        ));

        var response = mvc.perform(put("/kit/gerar-kits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(removeKitDTOJacksonTester.write(kit).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando kit estiver incompleto por falta de estoque")
    void createKitIncompleto() throws Exception {
        doThrow(CustomException.kitIncompleto(Map.of("Body", 2)))
                .when(itemService).createdKit(any());

        var kit = new RemoveKitDTO(KitType.ENXOVAL, List.of(
                new KitDTO("Body", Gender.M, "P", 2)
        ));

        var response = mvc.perform(put("/kit/gerar-kits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(removeKitDTOJacksonTester.write(kit).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 404 quando item do kit não for encontrado")
    void createKitItemNaoEncontrado() throws Exception {
        doThrow(CustomException.itemNaoEncontrado("Body - Tamanho: P - Gênero: M"))
                .when(itemService).createdKit(any());

        var kit = new RemoveKitDTO(KitType.ENXOVAL, List.of(
                new KitDTO("Body", Gender.M, "P", 2)
        ));

        var response = mvc.perform(put("/kit/gerar-kits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(removeKitDTOJacksonTester.write(kit).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando estoque for insuficiente para gerar kit")
    void createKitEstoqueInsuficiente() throws Exception {
        doThrow(CustomException.estoqueInsuficiente("Body", 1, 2))
                .when(itemService).createdKit(any());

        var kit = new RemoveKitDTO(KitType.ENXOVAL, List.of(
                new KitDTO("Body", Gender.M, "P", 2)
        ));

        var response = mvc.perform(put("/kit/gerar-kits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(removeKitDTOJacksonTester.write(kit).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}