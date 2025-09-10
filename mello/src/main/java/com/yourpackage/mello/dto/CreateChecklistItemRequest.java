package com.yourpackage.mello.dto;

import lombok.Data;

@Data
public class CreateChecklistItemRequest {
    private Long cardId;
    private String content;
    private boolean checked;
}
