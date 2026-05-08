package abraceumrn.com.api.infra.security;

import abraceumrn.com.api.domain.repository.UserRepository;
import abraceumrn.com.api.domain.user.UserAuthenticated;
import abraceumrn.com.api.domain.user.UserData;
import abraceumrn.com.api.infra.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserData user = userRepository.findByEmail(username)
                .orElseThrow(() -> CustomException.usuarioNaoEncontrado(username));
        return new UserAuthenticated(user);
    }
}
