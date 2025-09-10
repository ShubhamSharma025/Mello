package com.yourpackage.mello.repository;

import com.yourpackage.mello.model.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
    List<ChecklistItem> findByCardId(Long cardId);
}
