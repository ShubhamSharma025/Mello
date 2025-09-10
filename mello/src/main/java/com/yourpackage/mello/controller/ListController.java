package com.yourpackage.mello.controller;

import com.yourpackage.mello.dto.CardListDto;
import com.yourpackage.mello.dto.CreateListRequest;

import com.yourpackage.mello.service.CardListService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lists")
public class ListController {


    private final CardListService cardListService;

  

    @PostMapping
    public ResponseEntity<?> createList(@RequestBody CreateListRequest request) {
        CardListDto created= cardListService.createList(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
            public ResponseEntity<String> deleteList(@PathVariable Long id) {
                cardListService.deleteList(id);
                return ResponseEntity.ok("successfully deleted");
         }
    } 