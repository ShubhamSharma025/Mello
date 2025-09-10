package com.yourpackage.mello.dto;

import java.util.List;

import lombok.Data;
@Data
public class CardListDto {
    private Long id;
    private String name;
    private int position;
    private Long boardId; // Prevents circular reference
    private List<CardDto> cards;
}