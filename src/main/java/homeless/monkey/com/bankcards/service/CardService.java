package homeless.monkey.com.bankcards.service;

import homeless.monkey.com.bankcards.dto.CardCreationRequestDTO;
import homeless.monkey.com.bankcards.dto.CardCreationResponseDTO;
import homeless.monkey.com.bankcards.entity.BankCard;
import homeless.monkey.com.bankcards.entity.CardStatus;
import homeless.monkey.com.bankcards.repository.CardsRepository;
import homeless.monkey.com.bankcards.repository.UserRepository;
import homeless.monkey.com.bankcards.util.CardUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    public final static String BIN = "412550";

    private final CardsRepository cardsRepository;
    private final UserRepository userRepository;

    public CardService(CardsRepository cardsRepository, UserRepository userRepository) {
        this.cardsRepository = cardsRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CardCreationResponseDTO createCard(CardCreationRequestDTO dto){

        var user = userRepository.findById(dto.getOwnerId())
                .orElseThrow(()-> new IllegalArgumentException(String.format("Пользователя с ID %s не найдено", dto.getOwnerId())));

        var cardNumber = generateCardNumber();
        var card = new BankCard();
        card.setCardNumber(cardNumber);
        card.setExpirationDate(dto.getExpirationDate());
        card.setBalance(dto.getBalance());
        card.setCardStatus(CardStatus.ACTIVE);
        card.setOwner(user);
        cardsRepository.save(card);

        return new CardCreationResponseDTO(card.getId(),
                CardUtil.getMaskedCardNumber(cardNumber),
                card.getExpirationDate(),
                card.getBalance(),
                card.getCardStatus(),
                card.getOwner().getId());
    }

    public void deleteCard(Long cardId){
        var card = cardsRepository.findById(cardId)
                .orElseThrow(()-> new IllegalArgumentException("Карта с id:" + cardId + " не найдена"));

        cardsRepository.delete(card);
    }

    private String generateCardNumber() {
        int maxAttempts = 5;
        String cardNumber;
        for (int i = 0; i < maxAttempts; i++) {
            cardNumber = CardUtil.generateCardNumber(BIN);
            if(cardsRepository.findBankCardByCardNumber(cardNumber).isEmpty())
                return cardNumber;
        }

        throw new IllegalStateException("Не удалось сгенерировать уникальный номер карты после " + maxAttempts + " попыток");
    }
}
