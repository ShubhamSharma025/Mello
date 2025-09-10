package com.yourpackage.mello.dto;

import lombok.Data;

@Data
public class CreateLabelRequest {
    private String name;
    private String color;
    private Long cardId;
}
