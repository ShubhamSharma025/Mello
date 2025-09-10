package com.yourpackage.mello.controller;

import com.yourpackage.mello.model.Card;
import com.yourpackage.mello.model.Comment;
import com.yourpackage.mello.model.Member;
import com.yourpackage.mello.repository.CardRepository;
import com.yourpackage.mello.repository.CommentRepository;
import com.yourpackage.mello.repository.MemberRepository;
import com.yourpackage.mello.dto.CommentDto;
import com.yourpackage.mello.dto.CreateCommentRequest;
import com.yourpackage.mello.mapper.CommentMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    private final CommentRepository commentRepo;
    private final CardRepository cardRepo;
    private final MemberRepository memberRepo;

    public CommentController(CommentRepository commentRepo, CardRepository cardRepo, MemberRepository memberRepo) {
        this.commentRepo = commentRepo;
        this.cardRepo = cardRepo;
        this.memberRepo = memberRepo;
    }

    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long cardId) {
        List<Comment> comments = commentRepo.findByCardIdOrderByCreatedAtDesc(cardId);
        List<CommentDto> dtos = comments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CreateCommentRequest request) {
        Optional<Card> cardOpt = cardRepo.findById(request.getCardId());
        if (cardOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Card not found");
        }

        Member author = null;
        if (request.getAuthorId() != null) {
            author = memberRepo.findById(request.getAuthorId()).orElse(null);
        }

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setCreatedAt(LocalDateTime.now());
        comment.setCard(cardOpt.get());
        comment.setAuthor(author);

        Comment saved = commentRepo.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentMapper.toDto(saved));
    }
}
