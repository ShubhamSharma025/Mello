package com.yourpackage.mello.controller;

import com.yourpackage.mello.dto.CreateLabelRequest;
import com.yourpackage.mello.dto.LabelDto;
import com.yourpackage.mello.mapper.LabelMapper;
import com.yourpackage.mello.model.Card;
import com.yourpackage.mello.model.Label;
import com.yourpackage.mello.repository.CardRepository;
import com.yourpackage.mello.repository.LabelRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/labels")
@CrossOrigin(origins = "http://localhost:3000")
public class LabelController {

    private final LabelRepository labelRepo;
    private final CardRepository cardRepo;

    public LabelController(LabelRepository labelRepo, CardRepository cardRepo) {
        this.labelRepo = labelRepo;
        this.cardRepo = cardRepo;
    }

    // ✅ Get all labels for a card
    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<LabelDto>> getLabels(@PathVariable Long cardId) {
        List<Label> labels = labelRepo.findByCardId(cardId);
        List<LabelDto> dtos = labels.stream()
                                    .map(LabelMapper::toDto)
                                    .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ✅ Create a new label for a card, replacing any existing label
    @PostMapping
    public ResponseEntity<?> createLabel(@RequestBody CreateLabelRequest request) {
        Optional<Card> cardOpt = cardRepo.findById(request.getCardId());
        if (cardOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Card not found");
        }

        Card card = cardOpt.get();

        // ❌ Delete any existing labels for this card (enforcing one label per card)
        List<Label> existingLabels = labelRepo.findByCardId(card.getId());
        labelRepo.deleteAll(existingLabels);

        // 🟢 Determine label color based on special label names
        String labelName = request.getName().trim().toLowerCase();
        String finalColor = request.getColor();

        if (labelName.equals("done")) {
            finalColor = "#22c55e"; // green
        } else if (labelName.equals("urgent")) {
            finalColor = "#ef4444"; // red
        }

        // ✅ Create and save the new label
        Label label = new Label();
        label.setName(request.getName().trim());
        label.setColor(finalColor);
        label.setCard(card);

        Label saved = labelRepo.save(label);
        return ResponseEntity.ok(LabelMapper.toDto(saved));
    }

    // ✅ Delete a label by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLabel(@PathVariable Long id) {
        if (!labelRepo.existsById(id)) {
            return ResponseEntity.status(404).body("Label not found");
        }
        labelRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
