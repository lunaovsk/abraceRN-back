package abraceumrn.com.api.controller.user;

import abraceumrn.com.api.domain.dto.UserDTO;
import abraceumrn.com.api.domain.dto.UserRequestDTO;
import abraceumrn.com.api.domain.enumItem.Role;
import abraceumrn.com.api.domain.user.UserData;
import abraceumrn.com.api.infra.exception.CustomException;
import abraceumrn.com.api.infra.security.TokenService;
import abraceumrn.com.api.infra.security.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<UserDTO> userDTOJacksonTester;

    @Autowired
    private JacksonTester<UserRequestDTO> userRequestDTOJacksonTester;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationManager manager;

    @MockitoBean
    private TokenService tokenService;

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 201 quando conta for criada com sucesso")
    void createCenarioSucesso() throws Exception {
        var userData = new UserData("teste@email.com", "senha123", Role.USER);
        when(userService.createAccount(any())).thenReturn(userData);

        var response = mvc.perform(post("/login/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJacksonTester.write(
                                new UserDTO("teste@email.com", "senha123")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando username estiver em branco no cadastro")
    void createCenarioUsernameBranco() throws Exception {
        var response = mvc.perform(post("/login/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJacksonTester.write(
                                new UserDTO("", "senha123")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando password estiver em branco no cadastro")
    void createCenarioPasswordBranco() throws Exception {
        var response = mvc.perform(post("/login/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJacksonTester.write(
                                new UserDTO("teste@email.com", "")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @WithMockUser
    @DisplayName("Deveria devolver 400 quando email já estiver cadastrado")
    void createCenarioEmailJaCadastrado() throws Exception {
        doThrow(new CustomException("E-mail já cadastrado."))
                .when(userService).createAccount(any());

        var response = mvc.perform(post("/login/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJacksonTester.write(
                                new UserDTO("teste@email.com", "senha123")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria devolver 200 e token quando login for bem sucedido")
    void loginCenarioSucesso() throws Exception {
        var authentication = new UsernamePasswordAuthenticationToken("teste@email.com", null, List.of());
        when(manager.authenticate(any())).thenReturn(authentication);
        when(tokenService.generateToken(any())).thenReturn("token-jwt-mockado");

        var response = mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestDTOJacksonTester.write(
                                new UserRequestDTO("teste@email.com", "senha123")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("token-jwt-mockado");
    }

    @Test
    @DisplayName("Deveria devolver 400 quando credenciais forem inválidas")
    void loginCenarioCredenciaisInvalidas() throws Exception {
        doThrow(new BadCredentialsException("Credenciais inválidas"))
                .when(manager).authenticate(any());

        var response = mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestDTOJacksonTester.write(
                                new UserRequestDTO("teste@email.com", "senha-errada")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}