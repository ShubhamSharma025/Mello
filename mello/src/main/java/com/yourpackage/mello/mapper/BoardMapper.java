package com.yourpackage.mello.mapper;

import com.yourpackage.mello.dto.BoardDto;
import com.yourpackage.mello.dto.CardListDto;
import com.yourpackage.mello.model.Board;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardMapper {

    private final CardListMapper cardListMapper;

    public  BoardDto toDto(Board board) {
        if (board == null) return null;

        BoardDto dto = new BoardDto();
        dto.setId(board.getId());
        dto.setName(board.getName());

        if (board.getCardLists() != null) {
            dto.setCardLists(
                board.getCardLists().stream()
                    .map(cardListMapper::toDto)
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public Board toEntity(BoardDto dto) {
        if (dto == null) return null;

        Board board = new Board();
        board.setId(dto.getId());
        board.setName(dto.getName());

        // Optional: You can add cardLists here if needed
        return board;
    }

   
}
