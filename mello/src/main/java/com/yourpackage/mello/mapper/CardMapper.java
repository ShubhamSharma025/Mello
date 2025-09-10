package com.yourpackage.mello.mapper;

import com.yourpackage.mello.dto.*;
import com.yourpackage.mello.model.*;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public  CardDto toDto(Card card) {
        if (card == null) return null;

        CardDto dto = new CardDto();
        dto.setId(card.getId());
        dto.setTitle(card.getTitle());
        dto.setDescription(card.getDescription());
        dto.setDueDate(card.getDueDate());
        dto.setPosition(card.getPosition());

        if (card.getCardList() != null) {
            dto.setListId(card.getCardList().getId());
        }

        if (card.getChecklistItems() != null) {
            dto.setChecklistItems(
                card.getChecklistItems().stream()
                    .map(ChecklistItemMapper::toDto)
                    .collect(Collectors.toList())
            );
        }

        if (card.getComments() != null) {
            dto.setComments(
                card.getComments().stream()
                    .map(CommentMapper::toDto)
                    .collect(Collectors.toList())
            );
        }

        if (card.getLabels() != null) {
            dto.setLabels(
                card.getLabels().stream()
                    .map(LabelMapper::toDto)
                    .collect(Collectors.toList())
            );
        }

        if (card.getMembers() != null) {
            dto.setMembers(
                card.getMembers().stream()
                    .map(MemberMapper::toDto)
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public  Card toEntity(CardDto dto, CardList cardList) {
        if (dto == null) return null;

        Card card = new Card();
        card.setId(dto.getId());
        card.setTitle(dto.getTitle());
        card.setDescription(dto.getDescription());
        card.setDueDate(dto.getDueDate());
        card.setPosition(dto.getPosition());
        card.setCardList(cardList);

        // Nested objects are usually handled in the service layer
        return card;
    }
}
