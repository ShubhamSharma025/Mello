package com.yourpackage.mello.repository;

import com.yourpackage.mello.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label, Long> {
    List<Label> findByCardId(Long cardId);
}
