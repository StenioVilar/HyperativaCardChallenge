package com.hyperativa.card.controller;

import com.hyperativa.card.payload.request.CardRequest;
import com.hyperativa.card.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/cards")
@Tag(name="Cartões")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Adicionar um novo cartão", description = "Cria um novo cartão no sistema com base nos dados fornecidos.")
    @PostMapping()
    public ResponseEntity<?> addCard(@Valid @RequestBody CardRequest cardRequest) {
        try {
            boolean success = cardService.createCard(cardRequest.getCardNumber());

            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(Map.of(
                                "message", "Card successfully added",
                                "cardNumber", cardRequest.getCardNumber()
                        ));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Failed to add the card."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid or incomplete data."));
        }
    }

    @Operation(summary = "Fazer upload de um arquivo de cartões",
            description = "Processa um arquivo contendo cartões em lote e retorna informações sobre os cartões processados.")
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadCardFile(@RequestParam("file") MultipartFile file) {
        try {
            Map<String, Object> result = cardService.processCardFile(file);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "File successfully processed",
                            "batch", result.get("batch"),
                            "recordCount", result.get("recordCount"),
                            "savedCards", result.get("savedCardsCount")
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal error while processing the file."));
        }
    }


    @Operation(summary = "Consultar um cartão pelo número",
            description = "Verifica se o cartão fornecido é válido e está registrado no sistema.")
    @GetMapping("/{cardNumber}")
    public ResponseEntity<?> checkCard(
            @PathVariable String cardNumber) {
        return cardService.checkCard(cardNumber)
                .map(card -> ResponseEntity.ok("Card is valid. ID: " + card.getId()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found"));
    }
}