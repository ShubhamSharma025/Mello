package com.yourpackage.mello.controller;

import lombok.Data;

@Data
public class CreateMemberRequest {
    private String name;
    private Long cardId;
}
