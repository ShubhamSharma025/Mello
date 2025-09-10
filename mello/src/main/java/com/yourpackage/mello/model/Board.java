package com.yourpackage.mello.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id", nullable = false)
private User user;

    
    @OneToMany(mappedBy = "board",
             cascade = CascadeType.ALL,
              orphanRemoval = true)
    @OrderBy("position ASC")
    @Builder.Default
    @JsonManagedReference
    private List<CardList> cardLists = new ArrayList<>();
    
    // Convenience method
    public void addCardList(CardList cardList) {
        cardLists.add(cardList);
        cardList.setBoard(this);
    }
    public void removeCardList(CardList cardList){
        cardLists.remove(cardList);
        cardList.setBoard(null);
    }
}