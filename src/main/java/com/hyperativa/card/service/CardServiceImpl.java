package com.hyperativa.card.service;

import com.hyperativa.card.domain.Card;
import com.hyperativa.card.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardRepository cardRepository;

    public Boolean createCard(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }
        if (cardRepository.findByCardNumber(cardNumber).isPresent()) {
            return false;
        }
        Card card = new Card();
        card.setCardNumber(cardNumber);
        cardRepository.save(card);
        return true;
    }

    @Override
    public Optional<Card> checkCard(String cardNumber) {
        return cardRepository.findByCardNumber(cardNumber);
    }

    @Override
    public Map<String, Object> processCardFile(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        int recordCount = 0;
        String batch = null;
        int savedCardsCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("DESAFIO-HYPERATIVA")) {
                    batch = line.substring(37, 45).trim();
                    recordCount = Integer.parseInt(line.substring(45, 51).trim());
                } else if (line.startsWith("C")) {
                    int endIndex = line.indexOf(' ', 7);
                    if (endIndex == -1) {
                        endIndex = line.length();
                    }
                    String cardNumber = line.substring(7, endIndex).trim();

                    if (createCard(cardNumber)) {
                        savedCardsCount++;
                    }
                } else if (line.startsWith("LOTE")) {
                    String footerBatch = line.substring(0, 8).trim();
                    int footerRecordCount = Integer.parseInt(line.substring(8, 14).trim());

                    if (!footerBatch.equals(batch) || footerRecordCount != recordCount) {
                        throw new IllegalArgumentException("Inconsistency in the file: BATCH or record count do not match");
                    }
                }
            }

            result.put("batch", batch);
            result.put("recordCount", recordCount);
            result.put("savedCardsCount", savedCardsCount);

        } catch (Exception e) {
            throw new RuntimeException("Error processing the file.", e);
        }

        return result;
    }
}