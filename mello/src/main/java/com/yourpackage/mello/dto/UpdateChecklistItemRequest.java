package com.yourpackage.mello.dto;

import lombok.Data;

@Data
public class UpdateChecklistItemRequest {
    private String content;
    private Boolean checked;
}
