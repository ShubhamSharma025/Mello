package com.yourpackage.mello.dto;


public class CreateCardRequest {
    private String title;
    private String description; // ✅ Add this
    private Long listId;
    private Integer position;
    private String dueDate;

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

    public Long getListId() {
        return listId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }
     
    public Integer getPosition(){
        return position;
    }

    public void setPosition(Integer position){
        this.position=position;
    }

    public String getDueDate() {
    return dueDate;
    }

   public void setDueDate(String dueDate) {
    this.dueDate = dueDate;
   }

}
