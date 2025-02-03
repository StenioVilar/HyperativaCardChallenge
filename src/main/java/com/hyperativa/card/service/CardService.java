package com.hyperativa.card.service;

import com.hyperativa.card.domain.Card;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

public interface CardService {
    Boolean createCard(String cardNumber);
    Optional<Card> checkCard(String cardNumber);
    Map<String, Object> processCardFile(MultipartFile file);
}