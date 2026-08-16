package abraceumrn.com.api.domain.user;


import abraceumrn.com.api.domain.dto.UserDTO;
import abraceumrn.com.api.domain.enumItem.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade que representa um usuário do sistema.
 *
 * Armazena informações de autenticação e autorização,
 * incluindo nome de usuário, senha criptografada e role de acesso.
 */
@Table(name = "user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime expiresAt;
    private String loginCode;
    private LocalDateTime loginCodeExpiresAt;

    /**
     * Construtor que cria um usuário com credenciais e role.
     *
     * @param username nome de usuário ou email
     * @param password senha do usuário (deve ser criptografada antes de persistir)
     * @param role role/permissão do usuário, seguindo principio de menor privilégio.
     */
    public UserData (String username, String password) {
        this.username = username;
        this.password = password;
        this.role = Role.USER;
        this.isActive = true;
    }

}