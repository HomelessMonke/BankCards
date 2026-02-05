package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.CardCreationRequestDTO;
import homeless.monkey.com.bankcards.dto.CardCreationResponseDTO;
import homeless.monkey.com.bankcards.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("admin/card")
    @ResponseStatus(HttpStatus.CREATED)
    public CardCreationResponseDTO createCard(@Valid @RequestBody CardCreationRequestDTO dto){
        return cardService.createCard(dto);
    }
}
