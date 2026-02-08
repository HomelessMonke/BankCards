package homeless.monkey.com.bankcards.repository;

import homeless.monkey.com.bankcards.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CardsRepository extends JpaRepository<CardEntity, Long>, JpaSpecificationExecutor<CardEntity> {

    Optional<CardEntity> findBankCardByCardNumber(String cardNumber);
}
