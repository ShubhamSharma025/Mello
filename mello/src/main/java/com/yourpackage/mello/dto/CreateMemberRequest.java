package com.yourpackage.mello.dto;

import lombok.Data;

@Data
public class CreateMemberRequest {
    private Long cardId;
    private String name;
}
