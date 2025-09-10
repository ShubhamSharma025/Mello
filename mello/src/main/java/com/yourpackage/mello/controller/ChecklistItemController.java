package com.yourpackage.mello.controller;

import com.yourpackage.mello.model.Card;
import com.yourpackage.mello.model.ChecklistItem;
import com.yourpackage.mello.repository.CardRepository;
import com.yourpackage.mello.repository.ChecklistItemRepository;
import com.yourpackage.mello.service.ChecklistItemService;

import lombok.RequiredArgsConstructor;

import com.yourpackage.mello.dto.UpdateChecklistItemRequest;
import com.yourpackage.mello.mapper.ChecklistItemMapper;
import com.yourpackage.mello.dto.ChecklistItemDto;
import com.yourpackage.mello.dto.CreateChecklistItemRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/checklist")
@RequiredArgsConstructor
public class ChecklistItemController {

   private final ChecklistItemService checklistItemService;

    // ✅ Get all checklist items for a card
    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<ChecklistItemDto>> getItemsForCard(@PathVariable Long cardId) {
       List<ChecklistItemDto> items=checklistItemService.getItemsByCard(cardId)
                            .stream()
                            .map(ChecklistItemMapper::toDto)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(items);
    }

    // ✅ Create a new checklist item using DTO
    @PostMapping
    public ResponseEntity<ChecklistItemDto> createItem(@RequestBody CreateChecklistItemRequest request) {
         ChecklistItem created =checklistItemService.createItem(request);
         return ResponseEntity.status(HttpStatus.CREATED).body(ChecklistItemMapper.toDto(created));
    }

    // ✅ Update checklist item (content + checked)
    @PutMapping("/{id}")
    public ResponseEntity<ChecklistItemDto> updateItem(
                    @PathVariable Long id,
                   @RequestBody UpdateChecklistItemRequest request) {
        ChecklistItem updated=checklistItemService.updateItem(id, request);
        return ResponseEntity.ok(ChecklistItemMapper.toDto(updated));
    }

    // ✅ Delete checklist item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
       checklistItemService.deleteItem(id);
       return ResponseEntity.noContent().build();
    }
}
