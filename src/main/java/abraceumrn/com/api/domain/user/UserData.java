package abraceumrn.com.api.domain.user;


import abraceumrn.com.api.domain.dto.UserDTO;
import abraceumrn.com.api.domain.enumItem.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public UserData (String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}