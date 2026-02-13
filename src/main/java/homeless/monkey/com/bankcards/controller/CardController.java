package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.card.*;
import homeless.monkey.com.bankcards.entity.CardStatus;
import homeless.monkey.com.bankcards.service.CardService;
import homeless.monkey.com.bankcards.util.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Cards", description = "Работа с картами")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Создать карту (admin)")
    @PostMapping("/admin/cards")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponseDto createCard(@Valid @RequestBody CardCreationRequestDto dto){
        return cardService.createCard(dto);
    }

    @Operation(summary = "Удалить карту (admin)")
    @DeleteMapping("/admin/cards/{cardID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAnyCard(@PathVariable Long cardID){
        cardService.deleteCard(cardID);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Установить статус карты (admin)")
    @PatchMapping("/admin/cards/{cardID}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCardStatus(@PathVariable Long cardID, @Valid @RequestBody UpdateCardStatusDto dto){
        cardService.updateCardStatus(cardID, dto.status());
    }

    @Operation(summary = "Получить список всех карт (admin)")
    @GetMapping("/admin/cards")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CardResponseDto> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort){

        Pageable pageable = PageUtils.createPageable(page, size, sort);
        return cardService.getAllCards(pageable);
    }

    @Operation(summary = "Получить список карт пользователя (user)")
    @GetMapping("/user/cards")
    @PreAuthorize("hasRole('USER')")
    public Page<CardResponseDto> getMyCards(@Valid @RequestBody CardSearchRequestDto searchDto){
        return cardService.getUserCards(searchDto);
    }

    @Operation(summary = "Перевести баланс м/у картами пользователя (user)")
    @PatchMapping("/user/cards/transfer")
    @PreAuthorize("hasRole('USER')")
    public TransferResponseDto transferBalance(@Valid @RequestBody TransferRequestDto transferDto){
        return cardService.transferUserCardsBalance(transferDto);
    }

    @Operation(summary = "Заблокировать карту пользователя (user)")
    @PatchMapping("/user/cards/{id}/block")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> blockUserCard(@PathVariable Long id){
        cardService.updateUserCardStatus(id, CardStatus.BLOCKED);
        return ResponseEntity.noContent().build();
    }
}
