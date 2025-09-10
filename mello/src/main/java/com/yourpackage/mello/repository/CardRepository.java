package com.yourpackage.mello.repository;

import com.yourpackage.mello.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByCardListIdOrderByPositionAsc(Long cardListId);

    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.position = c.position + 1 " +
           "WHERE c.cardList.id = :cardListId AND c.position >= :position")
    void incrementPositions(@Param("cardListId") Long cardListId, @Param("position") int position);

    @Modifying
    @Transactional
    @Query("DELETE FROM Card c WHERE c.cardList.id = :cardListId")
    void deleteByCardListId(@Param("cardListId") Long cardListId);

      // ✅ New method: get highest position in a list
    @Query("SELECT COALESCE(MAX(c.position), -1) FROM Card c WHERE c.cardList.id = :cardListId")
    int findMaxPosition(@Param("cardListId") Long cardListId);

}
