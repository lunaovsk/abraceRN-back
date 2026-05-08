package abraceumrn.com.api.infra.security;

import abraceumrn.com.api.domain.dto.UserDTO;
import abraceumrn.com.api.domain.enumItem.Role;
import abraceumrn.com.api.domain.repository.UserRepository;
import abraceumrn.com.api.domain.user.UserAuthenticated;
import abraceumrn.com.api.domain.user.UserData;
import abraceumrn.com.api.infra.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> CustomException.usuarioNaoEncontrado(username));
        return new UserAuthenticated(user);
    }

    @Transactional
    public UserData createAccount(UserDTO dto) {
        var email = userRepository.existsByUsername(dto.username());
        if (email) {
            throw new CustomException("E-mail já cadastrado.");
        }
        String passwordEnconded = passwordEncoder.encode(dto.password());
        String emailLowerCase = dto.username().toLowerCase();
        UserData data = new UserData(emailLowerCase, passwordEnconded, Role.USER);
        return userRepository.save(data);
    }
}
