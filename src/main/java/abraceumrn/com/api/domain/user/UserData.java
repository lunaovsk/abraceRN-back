package abraceumrn.com.api.domain.user;


import abraceumrn.com.api.domain.dto.UserDTO;
import abraceumrn.com.api.domain.enumItem.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    /**
     * Construtor que cria um usuário com credenciais e role.
     *
     * @param username nome de usuário ou email
     * @param password senha do usuário (deve ser criptografada antes de persistir)
     * @param role role/permissão do usuário (USER ou ADMIN)
     */
    public UserData (String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}