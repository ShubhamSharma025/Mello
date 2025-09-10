package com.yourpackage.mello.dto;

import lombok.Data;

@Data
public class ReorderCardRequest {
    private Long cardId;
    private Long targetListId; // Optional if moving across lists
    private int newPosition;
}
