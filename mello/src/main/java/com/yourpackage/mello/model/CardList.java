package com.yourpackage.mello.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private int position;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board board;
    
    @OneToMany(mappedBy = "cardList", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    @Builder.Default
    @JsonManagedReference
    private List<Card> cards = new ArrayList<>();
    
    // Convenience method
    public void addCard(Card card) {
        cards.add(card);
        card.setCardList(this);
    }

    public void remove(Card card) {
      cards.remove(card);
      card.setCardList(null);

    }
}