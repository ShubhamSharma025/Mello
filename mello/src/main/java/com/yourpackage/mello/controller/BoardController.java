package com.yourpackage.mello.controller;

import com.yourpackage.mello.dto.BoardDto;
import com.yourpackage.mello.mapper.BoardMapper;
import com.yourpackage.mello.model.Board;
import com.yourpackage.mello.repository.BoardRepository;
import com.yourpackage.mello.service.BoardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

   private final BoardService boardService ;

    // Create a new board (still returns full entity)
    @PostMapping
    public ResponseEntity<BoardDto> createBoard(@RequestBody BoardDto boardDto,
                                                Authentication authentication) {
       String username=authentication.getName();
        BoardDto createdBoard = boardService .createBoard(boardDto,username);
        return new ResponseEntity<>(createdBoard, HttpStatus.CREATED);
    }

    // Get all boards (returns DTOs)
    @GetMapping
    public ResponseEntity<List<BoardDto>> getAllBoards(Authentication authentication) {
        String username=authentication.getName();
     return ResponseEntity.ok(boardService.getAllBoards(username));
    }

    // Get board by ID (returns DTO)
    @GetMapping("/{id}")
    public ResponseEntity<BoardDto> getBoardById(@PathVariable Long id,
                                                    Authentication authentication) {
       String username=authentication.getName();
       BoardDto board=boardService.getBoardById(id,username);
        return ResponseEntity.ok(board);
    }

    // Update board (returns DTO)
    @PutMapping("/{id}")
    public ResponseEntity<BoardDto> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardDto boardDto) {
                BoardDto updateBoard=boardService.updateBoard(id,boardDto);
            return ResponseEntity.ok(updateBoard);
    }

    // Delete a board
    @DeleteMapping("/{id}")
public ResponseEntity<Void> deleteBoard(@PathVariable Long id, Authentication authentication) {
    try {
        String username = authentication.getName();
        boardService.deleteBoard(id); // ✅ Pass username
        return ResponseEntity.noContent().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

}
