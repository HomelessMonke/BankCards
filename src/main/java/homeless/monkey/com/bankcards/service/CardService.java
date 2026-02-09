package homeless.monkey.com.bankcards.service;

import homeless.monkey.com.bankcards.dto.card.*;
import homeless.monkey.com.bankcards.entity.CardEntity;
import homeless.monkey.com.bankcards.entity.CardStatus;
import homeless.monkey.com.bankcards.entity.UserEntity;
import homeless.monkey.com.bankcards.repository.CardsRepository;
import homeless.monkey.com.bankcards.repository.UserRepository;
import homeless.monkey.com.bankcards.util.CardFilterSpecifications;
import homeless.monkey.com.bankcards.util.CardUtils;
import homeless.monkey.com.bankcards.util.PageUtils;
import jakarta.transaction.Transactional;
import homeless.monkey.com.bankcards.mapper.CardMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CardService {

    public final static String BIN = "412550";

    private final CardsRepository cardsRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CardMapper cardMapper;

    public CardService(CardsRepository cardsRepository, UserRepository userRepository, UserService userService, CardMapper cardMapper) {
        this.cardsRepository = cardsRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.cardMapper = cardMapper;
    }

    public Page<CardResponseDto> getAllCards(Pageable pageable){
        Page<CardEntity> cardsPage = cardsRepository.findAll(pageable);
        return cardsPage.map(cardMapper::toResponseDto);
    }

    public Page<CardResponseDto> getUserCards(CardSearchRequestDto searchDto){

        UserEntity user = userService.getCurrentUser();
        Pageable pageable = PageUtils.createPageable(searchDto.getPage(),searchDto.getSize(), searchDto.getSort());
        Specification<CardEntity> spec = CardFilterSpecifications.byOwnerId(user.getId());

        var filter = searchDto.getCardFilter();
        if(filter != null){
            spec = spec.and(CardFilterSpecifications.fromFilter(filter));
        }

        return cardsRepository.findAll(spec, pageable).map(cardMapper::toResponseDto);
    }

    @Transactional
    public TransferResponseDto transferUserCardsBalance(TransferRequestDto transferDto){

        if(Objects.equals(transferDto.getFromCardId(), transferDto.getToCardId()))
            throw new IllegalArgumentException("Дважды указана одна и та же карта");

        UserEntity user = userService.getCurrentUser();
        CardEntity fromCard = getCardById(transferDto.getFromCardId());
        CardEntity toCard = getCardById(transferDto.getToCardId());

        var userId = user.getId();
        if(!fromCard.belongsToUser(userId) || !toCard.belongsToUser(userId))
            throw new AccessDeniedException("Перевод только между картами авторизованного пользователя");

        if (!fromCard.isActiveCard() || !toCard.isActiveCard())
            throw new IllegalStateException("Одна из карт не активна");

        fromCard.subtractBalance(transferDto.getAmount());
        toCard.addBalance(transferDto.getAmount());
        cardsRepository.save(fromCard);
        cardsRepository.save(toCard);

        return new TransferResponseDto(
                CardUtils.getMaskedCardNumber(fromCard.getCardNumber()),
                CardUtils.getMaskedCardNumber(toCard.getCardNumber()),
                fromCard.getBalance(),
                toCard.getBalance());
    }

    public CardEntity getCardById(Long id){
        return cardsRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Карта с ID " + id + " не найдена"));
    }

    @Transactional
    public CardResponseDto createCard(CardCreationRequestDto dto){

        var user = userRepository.findById(dto.getOwnerId())
                .orElseThrow(()-> new IllegalArgumentException(String.format("Пользователя с ID %s не найдено", dto.getOwnerId())));

        var cardNumber = generateCardNumber();
        var card = new CardEntity();
        card.setCardNumber(cardNumber);
        card.setExpirationDate(dto.getExpirationDate());
        card.setBalance(dto.getBalance());
        card.setCardStatus(CardStatus.ACTIVE);
        card.setOwner(user);
        cardsRepository.save(card);

        return cardMapper.toResponseDto(card);
    }

    public void deleteCard(Long cardID){
        var card = getCard(cardID);
        cardsRepository.delete(card);
    }

    public void updateCardStatus(Long cardID, UpdateCardStatusDto dto){
        var card = getCard(cardID);
        card.setCardStatus(dto.status());
        cardsRepository.save(card);
    }

    private CardEntity getCard(Long cardID){
        return cardsRepository.findById(cardID)
                .orElseThrow(()-> new IllegalArgumentException("Карта с id:" + cardID + " не найдена"));
    }

    private String generateCardNumber() {
        int maxAttempts = 5;
        String cardNumber;
        for (int i = 0; i < maxAttempts; i++) {
            cardNumber = CardUtils.generateCardNumber(BIN);
            if(cardsRepository.findBankCardByCardNumber(cardNumber).isEmpty())
                return cardNumber;
        }

        throw new IllegalStateException("Не удалось сгенерировать уникальный номер карты после " + maxAttempts + " попыток");
    }
}
