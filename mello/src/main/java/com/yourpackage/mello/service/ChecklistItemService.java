package com.yourpackage.mello.service;

import com.yourpackage.mello.dto.CreateChecklistItemRequest;
import com.yourpackage.mello.dto.UpdateChecklistItemRequest;
import com.yourpackage.mello.model.Card;
import com.yourpackage.mello.model.ChecklistItem;
import com.yourpackage.mello.repository.CardRepository;
import com.yourpackage.mello.repository.ChecklistItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChecklistItemService {

    private final ChecklistItemRepository checklistItemRepository;
    private final CardRepository cardRepository;

    public ChecklistItemService(ChecklistItemRepository checklistItemRepository, CardRepository cardRepository) {
        this.checklistItemRepository = checklistItemRepository;
        this.cardRepository = cardRepository;
    }

    public List<ChecklistItem> getItemsByCard(Long cardId) {
        return checklistItemRepository.findByCardId(cardId);
    }

    public ChecklistItem createItem(CreateChecklistItemRequest request) {
        Optional<Card> cardOpt = cardRepository.findById(request.getCardId());
        if (cardOpt.isEmpty()) throw new RuntimeException("Card not found");

        ChecklistItem item = ChecklistItem.builder()
                .card(cardOpt.get())
                .content(request.getContent())
                .checked(false)
                .build();

        return checklistItemRepository.save(item);
    }

    public ChecklistItem updateItem(Long itemId, UpdateChecklistItemRequest request) {
        ChecklistItem item = checklistItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Checklist item not found"));

        if (request.getContent() != null) item.setContent(request.getContent());
        if (request.getChecked() != null) item.setChecked(request.getChecked());

        return checklistItemRepository.save(item);
    }

    public void deleteItem(Long itemId) {
        checklistItemRepository.deleteById(itemId);
    }
}
