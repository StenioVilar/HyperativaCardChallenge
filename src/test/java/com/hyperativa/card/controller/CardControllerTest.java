package com.hyperativa.card.controller;

import com.hyperativa.card.domain.Card;
import com.hyperativa.card.payload.request.CardRequest;
import com.hyperativa.card.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    void setUp() {
        Mockito.reset(cardService);
    }

    @Test
    void addCard_Success() {
        CardRequest request = new CardRequest();
        request.setCardNumber("123456789");
        when(cardService.createCard(request.getCardNumber())).thenReturn(true);

        ResponseEntity<?> response = cardController.addCard(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("cardNumber"));
        verify(cardService, times(1)).createCard(request.getCardNumber());
    }

    @Test
    void addCard_Failure() {
        CardRequest request = new CardRequest();
        request.setCardNumber("123456789");
        when(cardService.createCard(request.getCardNumber())).thenReturn(false);

        ResponseEntity<?> response = cardController.addCard(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(cardService, times(1)).createCard(request.getCardNumber());
    }

    @Test
    void addCard_Exception() {
        CardRequest request = new CardRequest();
        request.setCardNumber("123456789");
        when(cardService.createCard(request.getCardNumber())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = cardController.addCard(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void uploadCardFile_Success() {
        MockMultipartFile file = new MockMultipartFile("file", "cards.csv", "text/csv", "123456789".getBytes());
        Map<String, Object> mockResult = Map.of("batch", "001", "recordCount", 10, "savedCardsCount", 8);
        when(cardService.processCardFile(file)).thenReturn(mockResult);

        ResponseEntity<?> response = cardController.uploadCardFile(file);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).containsKey("batch"));
    }

    @Test
    void uploadCardFile_Exception() {
        MockMultipartFile file = new MockMultipartFile("file", "cards.csv", "text/csv", "123456789".getBytes());
        when(cardService.processCardFile(file)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = cardController.uploadCardFile(file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void checkCard_Valid() {
        Card card = new Card();
        card.setCardNumber("123456789");
        when(cardService.checkCard("123456789")).thenReturn(Optional.of(card));

        ResponseEntity<?> response = cardController.checkCard("123456789");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void checkCard_NotFound() {
        when(cardService.checkCard(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> response = cardController.checkCard("123456789");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Card not found", response.getBody());
    }
}