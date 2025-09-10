package com.yourpackage.mello.controller;

import com.yourpackage.mello.dto.CardDto;
import com.yourpackage.mello.dto.CreateCardRequest;
import com.yourpackage.mello.dto.ReorderCardRequest;
import com.yourpackage.mello.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // ✅ Create card
    @PostMapping
    public ResponseEntity<CardDto> createCard(@RequestBody CreateCardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(request));
    }

    // ✅ Reorder card
    @PutMapping("/reorder")
    public ResponseEntity<Void> reorderCard(@RequestBody ReorderCardRequest request) {
        cardService.reorderCard(request);
        return ResponseEntity.ok().build();
    }

    // ✅ Update card
    @PutMapping("/{id}")
    public ResponseEntity<CardDto> updateCard(@PathVariable Long id, @RequestBody CreateCardRequest request) {
        return ResponseEntity.ok(cardService.updateCard(id, request));
    }

    // ✅ Delete card
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        try {
            cardService.deleteCard(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
