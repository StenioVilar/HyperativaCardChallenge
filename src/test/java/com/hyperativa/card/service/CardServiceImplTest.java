package com.hyperativa.card.service;

import com.hyperativa.card.domain.Card;
import com.hyperativa.card.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        reset(cardRepository);
    }

    @Test
    void createCard_Success() {
        String cardNumber = "123456789";
        when(cardRepository.findByCardNumber(cardNumber)).thenReturn(Optional.empty());
        when(cardRepository.save(any(Card.class))).thenReturn(new Card());

        Boolean result = cardService.createCard(cardNumber);

        assertTrue(result);
        verify(cardRepository, times(1)).findByCardNumber(cardNumber);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_Failure_ExistingCard() {
        String cardNumber = "123456789";
        when(cardRepository.findByCardNumber(cardNumber)).thenReturn(Optional.of(new Card()));

        Boolean result = cardService.createCard(cardNumber);

        assertFalse(result);
        verify(cardRepository, times(1)).findByCardNumber(cardNumber);
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void createCard_Failure_InvalidCardNumber() {
        String invalidCardNumber = "   ";

        Boolean result = cardService.createCard(invalidCardNumber);

        assertFalse(result);
        verify(cardRepository, never()).findByCardNumber(anyString());
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void checkCard_Success() {
        String cardNumber = "123456789";
        Card mockCard = new Card();
        mockCard.setCardNumber(cardNumber);
        when(cardRepository.findByCardNumber(cardNumber)).thenReturn(Optional.of(mockCard));

        Optional<Card> cardOptional = cardService.checkCard(cardNumber);

        assertTrue(cardOptional.isPresent());
        assertEquals(cardNumber, cardOptional.get().getCardNumber());
        verify(cardRepository, times(1)).findByCardNumber(cardNumber);
    }

    @Test
    void checkCard_NotFound() {
        String cardNumber = "123456789";
        when(cardRepository.findByCardNumber(cardNumber)).thenReturn(Optional.empty());

        Optional<Card> cardOptional = cardService.checkCard(cardNumber);

        assertFalse(cardOptional.isPresent());
        verify(cardRepository, times(1)).findByCardNumber(cardNumber);
    }

}