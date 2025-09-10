package com.yourpackage.mello.mapper;

import com.yourpackage.mello.model.ChecklistItem;
import com.yourpackage.mello.dto.ChecklistItemDto;

public class ChecklistItemMapper {

    public static ChecklistItemDto toDto(ChecklistItem entity) {
        if (entity == null) return null;

        ChecklistItemDto dto = new ChecklistItemDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setChecked(entity.isChecked());
        return dto;
    }

    public static ChecklistItem toEntity(ChecklistItemDto dto) {
        if (dto == null) return null;

        ChecklistItem entity = new ChecklistItem();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setChecked(dto.isChecked());
        return entity;
    }
}
