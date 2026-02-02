package homeless.monkey.com.bankcards.repository;

import homeless.monkey.com.bankcards.entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankCardsRepository extends JpaRepository<BankCard, Long> {

}
