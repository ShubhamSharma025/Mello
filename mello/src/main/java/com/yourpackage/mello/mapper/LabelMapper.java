package com.yourpackage.mello.mapper;

import com.yourpackage.mello.dto.LabelDto;
import com.yourpackage.mello.model.Label;

public class LabelMapper {

    public static LabelDto toDto(Label entity) {
        if (entity == null) return null;

        LabelDto dto = new LabelDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setColor(entity.getColor());

        // ✅ Set cardId from the associated Card (to avoid full card serialization)
        if (entity.getCard() != null) {
            dto.setCardId(entity.getCard().getId());
        }

        return dto;
    }

    public static Label toEntity(LabelDto dto) {
        if (dto == null) return null;

        Label entity = new Label();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setColor(dto.getColor());

        // ❌ Do NOT set Card here — handled by controller (to avoid lazy loading issues)
        return entity;
    }
}
