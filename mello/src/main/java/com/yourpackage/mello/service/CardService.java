package com.yourpackage.mello.service;

import com.yourpackage.mello.dto.CardDto;
import com.yourpackage.mello.dto.CreateCardRequest;
import com.yourpackage.mello.dto.ReorderCardRequest;
import com.yourpackage.mello.mapper.CardMapper;
import com.yourpackage.mello.model.Card;
import com.yourpackage.mello.model.CardList;
import com.yourpackage.mello.repository.CardRepository;
import com.yourpackage.mello.repository.CardListRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardMapper cardMapper;
    private final CardRepository cardRepository;
    private final CardListRepository cardListRepository;

    // ✅ Create card
    public CardDto createCard(CreateCardRequest request) {
        CardList list = cardListRepository.findById(request.getListId())
                .orElseThrow(() -> new EntityNotFoundException("List not found"));

        int position = (request.getPosition() != null)
                ? request.getPosition()
                : cardRepository.findMaxPosition(list.getId()) + 1;

        // Shift cards if inserting in middle
        cardRepository.incrementPositions(list.getId(), position);

        Card card = new Card();
        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());
        card.setCardList(list);
        card.setPosition(position);

        if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
            card.setDueDate(LocalDate.parse(request.getDueDate()));
        }

        return cardMapper.toDto(cardRepository.save(card));
    }

    // ✅ Reorder card (drag-and-drop)
    public void reorderCard(ReorderCardRequest request) {
        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        Long oldListId = card.getCardList().getId();
        Long targetListId = request.getTargetListId();

        // Remove from old list
        if (!oldListId.equals(targetListId)) {
            List<Card> oldCards = cardRepository.findByCardListIdOrderByPositionAsc(oldListId);
            oldCards.remove(card);
            for (int i = 0; i < oldCards.size(); i++) {
                oldCards.get(i).setPosition(i);
            }
            cardRepository.saveAll(oldCards);

            // Move to new list
            CardList newList = cardListRepository.findById(targetListId)
                    .orElseThrow(() -> new EntityNotFoundException("Target list not found"));
            card.setCardList(newList);
        }

        // Reorder in target list
        card.setPosition(request.getNewPosition());
        List<Card> targetCards = cardRepository.findByCardListIdOrderByPositionAsc(targetListId);
        targetCards.removeIf(c -> c.getId().equals(card.getId()));
        targetCards.add(request.getNewPosition(), card);

        for (int i = 0; i < targetCards.size(); i++) {
            targetCards.get(i).setPosition(i);
        }

        cardRepository.saveAll(targetCards);
    }

    // ✅ Update card
    public CardDto updateCard(Long id, CreateCardRequest request) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        card.setTitle(request.getTitle());
        card.setDescription(request.getDescription());

        if (request.getListId() != null && !request.getListId().equals(card.getCardList().getId())) {
            CardList newList = cardListRepository.findById(request.getListId())
                    .orElseThrow(() -> new EntityNotFoundException("Target list not found"));
            card.setCardList(newList);
        }

        if (request.getDueDate() != null && !request.getDueDate().isEmpty()) {
            card.setDueDate(LocalDate.parse(request.getDueDate()));
        }

        return cardMapper.toDto(cardRepository.save(card));
    }

    // ✅ Delete card
    public void deleteCard(Long id) {
        if (!cardRepository.existsById(id)) {
            throw new EntityNotFoundException("Card not found");
        }
        cardRepository.deleteById(id);
    }
}
