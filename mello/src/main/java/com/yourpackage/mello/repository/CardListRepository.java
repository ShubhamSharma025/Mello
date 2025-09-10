package com.yourpackage.mello.repository;

import com.yourpackage.mello.model.CardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardListRepository extends JpaRepository<CardList, Long> {

    //  Get lists for a board ordered by position
    List<CardList> findByBoardIdOrderByPositionAsc(Long boardId);

    // Fetch lists with their cards in one query (avoids N+1)
    @Query("SELECT DISTINCT cl FROM CardList cl " +
           "LEFT JOIN FETCH cl.cards " +
           "WHERE cl.board.id = :boardId " +
           "ORDER BY cl.position ASC")
    List<CardList> findByBoardIdWithCards(@Param("boardId") Long boardId);

    // Increment positions when inserting a list in the middle
    @Modifying
    @Query("UPDATE CardList cl SET cl.position = cl.position + 1 " +
           "WHERE cl.board.id = :boardId AND cl.position >= :position")
    void incrementPositions(@Param("boardId") Long boardId,
                            @Param("position") int position);
}
