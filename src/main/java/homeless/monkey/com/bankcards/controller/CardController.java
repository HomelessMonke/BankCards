package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.card.*;
import homeless.monkey.com.bankcards.entity.CardStatus;
import homeless.monkey.com.bankcards.service.CardService;
import homeless.monkey.com.bankcards.service.UserService;
import homeless.monkey.com.bankcards.util.PageUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/admin/cards")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponseDto createCard(@Valid @RequestBody CardCreationRequestDto dto){
        return cardService.createCard(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/cards/{cardID}")
    public ResponseEntity<Void> deleteAnyCard(@PathVariable Long cardID){
        cardService.deleteCard(cardID);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/admin/cards/{cardID}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateCardStatus(@PathVariable Long cardID, @Valid @RequestBody UpdateCardStatusDto dto){
        cardService.updateCardStatus(cardID, dto.status());
    }

    @GetMapping("/admin/cards")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CardResponseDto> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort){

        Pageable pageable = PageUtils.createPageable(page, size, sort);
        return cardService.getAllCards(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/cards")
    public Page<CardResponseDto> getMyCards(@Valid @RequestBody CardSearchRequestDto searchDto){
        return cardService.getUserCards(searchDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/user/cards/transfer")
    public TransferResponseDto transferBalance(@Valid @RequestBody TransferRequestDto transferDto){
        return cardService.transferUserCardsBalance(transferDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/user/cards/{id}/block")
    public ResponseEntity<Void> blockUserCard(@PathVariable Long id){
        cardService.updateUserCardStatus(id, CardStatus.BLOCKED);
        return ResponseEntity.noContent().build();
    }
}
