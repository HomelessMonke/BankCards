package homeless.monkey.com.bankcards.repository;

import homeless.monkey.com.bankcards.entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardsRepository extends JpaRepository<BankCard, Long> {

    Optional<BankCard> findBankCardByCardNumber(String cardNumber);
}
