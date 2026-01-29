package abraceumrn.com.api.domain.user;


import abraceumrn.com.api.domain.enumItem.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "user")
@Entity
@Getter
@Setter
public class UserData {

    @Id
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;


    public UserData () {

    }
    public UserData (UserDTO userDTO) {
        this.username = userDTO.username();
        this.password = userDTO.password();
        this.role = userDTO.role();
    }

}
