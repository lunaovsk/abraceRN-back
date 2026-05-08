package abraceumrn.com.api.controller.user;

import abraceumrn.com.api.domain.dto.UserDTO;
import abraceumrn.com.api.domain.dto.UserRequestDTO;
import abraceumrn.com.api.domain.dto.UserResponse;
import abraceumrn.com.api.domain.user.UserData;
import abraceumrn.com.api.infra.security.TokenJWT;
import abraceumrn.com.api.infra.security.TokenService;
import abraceumrn.com.api.infra.security.UserService;
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
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;


    @PostMapping("/create")
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserDTO userDTO) {
        var account = userService.createAccount(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse(account.getUsername(), account.getRole()));
    }

    @PostMapping
    public ResponseEntity<TokenJWT> login(@RequestBody @Valid UserRequestDTO requestDTO) {
        var credentials = new UsernamePasswordAuthenticationToken(requestDTO.username(), requestDTO.password());
        var authenticated = manager.authenticate(credentials);
        var token = tokenService.generateToken(authenticated);
        return ResponseEntity.ok().body(new TokenJWT(token));
    }
}
