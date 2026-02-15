package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.card.*;
import homeless.monkey.com.bankcards.entity.CardStatus;
import homeless.monkey.com.bankcards.service.CardService;
import homeless.monkey.com.bankcards.service.UserService;
import homeless.monkey.com.bankcards.util.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cards", description = "Работа с картами")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Создать карту (admin)")
    @PostMapping("/admin/cards")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CardResponseDto> createCard(@Valid @RequestBody CardCreationRequestDto dto){
        log.info("POST /admin/cards: создание карты для ownerId={}, balance={}, validityYears={}",
                dto.getOwnerId(), dto.getBalance(), dto.getValidityYears());

        CardResponseDto responseDto = cardService.createCard(dto);
        log.info("Успеное создание карты: maskedCardNumber={}, ownerId={}, expirationDate={}",
                responseDto.maskedCardNumber(), responseDto.ownerId(), responseDto.expirationDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Удалить карту (admin)")
    @DeleteMapping("/admin/cards/{cardId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnyCard(@PathVariable Long cardId){
        log.info("DELETE /admin/cards/{}: запрос удаления карты", cardId);
        cardService.deleteCard(cardId);
        log.info("Успешное удаление карты: cardId={}", cardId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Установить статус карты (admin)")
    @PatchMapping("/admin/cards/{cardId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateCardStatus(@PathVariable Long cardId, @Valid @RequestBody UpdateCardStatusDto dto){
        log.info("PATCH /admin/cards/{}/status: новый статус = {}", cardId, dto.status());
        cardService.updateCardStatus(cardId, dto.status());
        log.info("Успешная смена статуса карты: cardId={}, status={})", cardId, dto.status());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить список всех карт (admin)")
    @GetMapping("/admin/cards")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CardResponseDto>> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort){

        log.info("GET /admin/cards: page={}, size={}, sort={}", page, size, sort);
        Pageable pageable = PageUtils.createPageable(page, size, sort);
        Page<CardResponseDto> response = cardService.getAllCards(pageable);
        log.info("Успешно возвращено {} карт (страница {})", response.getNumberOfElements(), page);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Получить список карт пользователя (user)")
    @GetMapping("/user/cards")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<CardResponseDto>> getMyCards(@Valid @RequestBody CardSearchRequestDto dto){
        log.info("GET /user/cards: page={}, size={}, sort={}, filter={}",
                dto.getPage(), dto.getSize(), dto.getSort(), dto.getCardFilter() != null ? "применён" : "нет");

        Page<CardResponseDto> response = cardService.getUserCards(dto);
        log.info("Успешно возвращено {} карт", response.getNumberOfElements());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Перевести баланс м/у картами пользователя (user)")
    @PatchMapping("/user/cards/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransferResponseDto> transferBalance(@Valid @RequestBody TransferRequestDto dto){
        log.info("PATCH /user/cards/transfer: м/у картами {} -> {} на сумму {}",
                dto.getFromCardId(), dto.getToCardId(), dto.getAmount());

        TransferResponseDto responseDto = cardService.transferUserCardsBalance(dto);
        log.info("Успешный перевод: {} -> {} на сумму {}",
                dto.getFromCardId(), dto.getToCardId(), dto.getAmount());

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Заблокировать карту пользователя (user)")
    @PatchMapping("/user/cards/{id}/block")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> blockUserCard(@PathVariable Long id){
        log.info("PATCH /user/cards/{}/block: запрос блокировки карты", id);
        cardService.updateUserCardStatus(id, CardStatus.BLOCKED);
        log.info("Успешная блокировка карты: cardId={}", id);
        return ResponseEntity.noContent().build();
    }
}
