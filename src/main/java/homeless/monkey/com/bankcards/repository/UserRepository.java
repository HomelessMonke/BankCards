package homeless.monkey.com.bankcards.repository;

import homeless.monkey.com.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}