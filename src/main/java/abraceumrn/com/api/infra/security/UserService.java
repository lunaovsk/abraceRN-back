package abraceumrn.com.api.infra.security;

import abraceumrn.com.api.domain.dto.UserDTO;
import abraceumrn.com.api.domain.dto.UserRequestDTO;
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

/**
 * Service de gerenciamento de usuários e autenticação.
 *
 * Implementa UserDetailsService para integração com Spring Security.
 * Responsável por: carregar usuários para autenticação e criar novas contas.
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Carrega detalhes do usuário pelo username para autenticação.
     *
     * Implementação de UserDetailsService.
     *
     * @param username email/username do usuário
     * @return UserDetails do usuário encontrado
     * @throws UsernameNotFoundException se o usuário não existir
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = userRepository.findByUsername(username)
                .orElseThrow(() -> CustomException.usuarioNaoEncontrado(username));
        return new UserAuthenticated(user);
    }

    /**
     * Cria uma nova conta de usuário no sistema.
     *
     * Validações:
     * - Verifica se o email já está cadastrado
     * - Criptografa a senha com BCrypt
     * - Converte email para minúsculas
     * - Atribui role USER por padrão
     *
     * @param dto dados de registro (username e password)
     * @return usuário criado e persistido
     * @throws CustomException se o email já existe
     */
    @Transactional
    public UserData createAccount(UserDTO dto) {
        var email = userRepository.existsByUsername(dto.username());
        if (email) {
            throw new CustomException("E-mail já cadastrado.");
        }
        String passwordEncoded = passwordEncoder.encode(dto.password());
        String emailLowerCase = dto.username().toLowerCase();
        UserData data = new UserData(emailLowerCase, passwordEncoded, Role.USER);
        return userRepository.save(data);
    }

}
