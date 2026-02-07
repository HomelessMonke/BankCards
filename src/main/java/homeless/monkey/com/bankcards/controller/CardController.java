package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.CardCreationRequestDTO;
import homeless.monkey.com.bankcards.dto.CardResponseDTO;
import homeless.monkey.com.bankcards.dto.UpdateCardStatusDTO;
import homeless.monkey.com.bankcards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public CardResponseDTO createCard(@Valid @RequestBody CardCreationRequestDTO dto){
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
    public void updateCardStatus(@PathVariable Long cardID, @Valid @RequestBody UpdateCardStatusDTO dto){
        cardService.updateCardStatus(cardID, dto);
    }

    @GetMapping("/admin/cards")
    @PreAuthorize("hasRole('ADMIN')")
    public Page<CardResponseDTO> getAllCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort){

        Sort sortObj = Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return cardService.getAllCards(pageable);
    }
}
