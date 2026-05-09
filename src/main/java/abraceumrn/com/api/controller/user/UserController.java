package abraceumrn.com.api.controller.user;

import abraceumrn.com.api.domain.dto.UserDTO;
import abraceumrn.com.api.domain.dto.UserRequestDTO;
import abraceumrn.com.api.domain.dto.UserResponse;
import abraceumrn.com.api.domain.user.UserData;
import abraceumrn.com.api.infra.security.TokenJWT;
import abraceumrn.com.api.infra.security.TokenService;
import abraceumrn.com.api.infra.security.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/login")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;


    @PostMapping("/create")
    @Operation(summary = "Cadastrar nova conta.", description = "Cria um novo cadastro no gerenciamento do estoque com as informações fornecidas.")
    @ApiResponse(responseCode = "201", description = "Conta cadastrada com sucesso!")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro!")
    @ApiResponse(responseCode = "404", description = "E-mail ou senha inválidos!")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor!")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserDTO userDTO) {
        var account = userService.createAccount(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(account.getUsername(), account.getRole()));
    }

    @PostMapping
    @Operation(summary = "Login no dashboard.", description = "Realizar login como user ou admin dependendo do nível de acesso inserido no sistema.")
    @ApiResponse(responseCode = "201", description = "Login efetuado com sucesso!")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para login!")
    @ApiResponse(responseCode = "404", description = "E-mail ou senha inválidos!")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor!")
    public ResponseEntity<TokenJWT> login(@RequestBody @Valid UserRequestDTO requestDTO) {
        var credentials = new UsernamePasswordAuthenticationToken(requestDTO.username(), requestDTO.password());
        var authenticated = manager.authenticate(credentials);
        var token = tokenService.generateToken(authenticated);
        return ResponseEntity.ok().body(new TokenJWT(token));
    }
}
