package com.yourpackage.mello.mapper;

import com.yourpackage.mello.dto.CardListDto;
import com.yourpackage.mello.model.CardList;

import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardListMapper {

 private final CardMapper cardMapper; 

    public  CardListDto toDto(CardList cardList) {
        if (cardList == null) return null;

        CardListDto dto = new CardListDto();
        dto.setId(cardList.getId());
        dto.setName(cardList.getName());
        dto.setPosition(cardList.getPosition());

        if (cardList.getBoard() != null) {
            dto.setBoardId(cardList.getBoard().getId());
        }

        if (cardList.getCards() != null) {
            dto.setCards(cardList.getCards().stream()
                    .map(cardMapper::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public  CardList toEntity(CardListDto dto) {
        if (dto == null) return null;

        CardList entity = new CardList();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPosition(dto.getPosition());
        // board will be set in the service layer
        return entity;
    }

    
}
