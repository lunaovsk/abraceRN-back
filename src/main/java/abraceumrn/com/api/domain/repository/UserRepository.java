package abraceumrn.com.api.domain.repository;

import abraceumrn.com.api.domain.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserData, String>{
    Optional<UserData> findByUsername(String username);

}
