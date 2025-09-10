package com.yourpackage.mello.dto;

import java.time.LocalDateTime;

public class CreateCommentRequest {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long cardId;
    private Long authorId;

    public CreateCommentRequest() {
        // Default constructor required for JSON deserialization
    }

    public CreateCommentRequest(Long id, String content, LocalDateTime createdAt, Long cardId, Long authorId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.cardId = cardId;
        this.authorId = authorId;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
