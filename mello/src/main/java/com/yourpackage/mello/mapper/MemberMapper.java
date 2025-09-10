package com.yourpackage.mello.mapper;

import com.yourpackage.mello.dto.MemberDto;
import com.yourpackage.mello.model.Member;

public class MemberMapper {

    public static MemberDto toDto(Member entity) {
        if (entity == null) return null;

        MemberDto dto = new MemberDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAvatarUrl(entity.getAvatarUrl());
        return dto;
    }

    public static Member toEntity(MemberDto dto) {
        if (dto == null) return null;

        Member entity = new Member();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setAvatarUrl(dto.getAvatarUrl());
        return entity;
    }
}
