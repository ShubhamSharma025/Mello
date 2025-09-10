package com.yourpackage.mello.dto;

import java.time.LocalDate;
import java.util.List;

public class CardDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private int position;

    private Long listId; // Just the ID of the CardList (to avoid infinite nesting)

    private List<ChecklistItemDto> checklistItems;
    private List<CommentDto> comments;
    private List<LabelDto> labels;
    private List<MemberDto> members;

    // Constructors
    public CardDto() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public List<ChecklistItemDto> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(List<ChecklistItemDto> checklistItems) {
        this.checklistItems = checklistItems;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public List<LabelDto> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelDto> labels) {
        this.labels = labels;
    }

    public List<MemberDto> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDto> members) {
        this.members = members;
    }
}
