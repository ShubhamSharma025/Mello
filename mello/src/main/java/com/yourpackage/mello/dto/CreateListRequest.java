package com.yourpackage.mello.dto;

public class CreateListRequest {
    
    private String name;
    private Long boardId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

}
