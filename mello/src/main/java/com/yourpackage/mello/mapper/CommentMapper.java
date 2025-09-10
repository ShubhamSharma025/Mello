package com.yourpackage.mello.mapper;

import com.yourpackage.mello.model.Comment;
import com.yourpackage.mello.model.Member;
import com.yourpackage.mello.dto.CommentDto;

public class CommentMapper {

    public static CommentDto toDto(Comment entity) {
        if (entity == null) return null;

        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setCreatedAt(entity.getCreatedAt());

        if (entity.getAuthor() != null) {
            dto.setAuthorName(entity.getAuthor().getName());
            dto.setAuthorId(entity.getAuthor().getId());
        }

        return dto;
    }

    public static Comment toEntity(CommentDto dto) {
        if (dto == null) return null;

        Comment entity = new Comment();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setCreatedAt(dto.getCreatedAt());

        // Author is not fetched here — must be set manually in the service
        // using something like: entity.setAuthor(memberRepository.findById(dto.getAuthorId()).orElseThrow(...));

        return entity;
    }

    // Optional helper if you want to pass the Member directly
    public static Comment toEntity(CommentDto dto, Member author) {
        Comment entity = toEntity(dto);
        entity.setAuthor(author);
        return entity;
    }
}
