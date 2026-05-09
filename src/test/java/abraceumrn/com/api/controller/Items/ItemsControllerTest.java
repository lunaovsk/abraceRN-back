package abraceumrn.com.api.controller.Items;

import abraceumrn.com.api.domain.dto.ViewItems;
import abraceumrn.com.api.domain.enumItem.Gender;
import abraceumrn.com.api.domain.enumItem.ItemType;
import abraceumrn.com.api.domain.items.ItemDTO;
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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ItemsControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JacksonTester<ItemDTO> itemDTOJacksonTester;
    @Autowired
    private JacksonTester<ViewItems> viewItemsJacksonTester;
    @MockitoBean
    private ItemService ItemService;

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando roupa não tiver tamanho e gênero")
    void registerCenarioRoupaSemTamanhoEGenero() throws Exception {
        doThrow(CustomException.validacao("Roupas devem conter tamanho e gênero."))
                .when(ItemService).registerItems(any());

        var response = mvc
                .perform(
                        post("/dashboard").contentType(MediaType.APPLICATION_JSON)
                                .content(itemDTOJacksonTester.write(
                                        new ItemDTO("Body", ItemType.ROUPA, null, 5, null, null)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando roupa não tiver tamanho")
    void registerCenarioRoupaSemTamanho() throws Exception {
        doThrow(CustomException.validacao("Roupas devem conter tamanho e gênero."))
                .when(ItemService).registerItems(any());

        var response = mvc
                .perform(
                        post("/dashboard").contentType(MediaType.APPLICATION_JSON)
                                .content(itemDTOJacksonTester.write(
                                        new ItemDTO("Body", ItemType.ROUPA, null, 5, Gender.M, null)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando roupa não tiver gênero")
    void registerCenarioRoupaSemGenero() throws Exception {
        doThrow(CustomException.validacao("Roupas devem conter tamanho e gênero."))
                .when(ItemService).registerItems(any());

        var response = mvc
                .perform(
                        post("/dashboard").contentType(MediaType.APPLICATION_JSON)
                                .content(itemDTOJacksonTester.write(
                                        new ItemDTO("Body", ItemType.ROUPA, "P", 5, null, null)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando higiene não tiver data de validade")
    void registerCenarioHigieneSemValidade() throws Exception {
        doThrow(CustomException.validacao("Itens de higiene e alimentação devem conter data de validade."))
                .when(ItemService).registerItems(any());

        var response = mvc
                .perform(
                        post("/dashboard").contentType(MediaType.APPLICATION_JSON)
                                .content(itemDTOJacksonTester.write(
                                        new ItemDTO("Fralda", ItemType.HIGIENE, null, 5, null, null)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando alimentação não tiver data de validade")
    void registerCenarioAlimentacaoSemValidade() throws Exception {
        doThrow(CustomException.validacao("Itens de higiene e alimentação devem conter data de validade."))
                .when(ItemService).registerItems(any());

        var response = mvc
                .perform(
                        post("/dashboard").contentType(MediaType.APPLICATION_JSON)
                                .content(itemDTOJacksonTester.write(
                                        new ItemDTO("Leite", ItemType.ALIMENTACAO, null, 10, null, null)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando quantidade for negativa")
    void registerCenarioQuantidadeNegativa() throws Exception {
        doThrow(CustomException.validacao("Quantidade deve ser maior ou igual a 0."))
                .when(ItemService).registerItems(any());

        var response = mvc
                .perform(
                        post("/dashboard").contentType(MediaType.APPLICATION_JSON)
                                .content(itemDTOJacksonTester.write(
                                        new ItemDTO("camisa", ItemType.ROUPA, "G", -1, Gender.M, null)
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 201 quando cadastrar item de higiene com data de validade")
    void registerCenarioCadastrarHigiene() throws Exception {
        var response = mvc
                .perform(
                        post("/dashboard").contentType(MediaType.APPLICATION_JSON)
                                .content(itemDTOJacksonTester.write(
                                        new ItemDTO("Fralda", ItemType.HIGIENE, null, 5, null, LocalDate.of(2026, 12, 31))
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 201 quando cadastrar item de alimentação com data de validade")
    void registerCenarioCadastrarAlimentacao() throws Exception {
        var response = mvc
                .perform(
                        post("/dashboard").contentType(MediaType.APPLICATION_JSON)
                                .content(itemDTOJacksonTester.write(
                                        new ItemDTO("Leite", ItemType.ALIMENTACAO, null, 10, null, LocalDate.of(2026, 6, 30))
                                ).getJson())
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }
}