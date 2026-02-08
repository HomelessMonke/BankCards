package homeless.monkey.com.bankcards.repository;

import homeless.monkey.com.bankcards.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardsRepository extends JpaRepository<CardEntity, Long> {

    Optional<CardEntity> findBankCardByCardNumber(String cardNumber);
}
