package com.yourpackage.mello.controller;

import com.yourpackage.mello.model.Card;
import com.yourpackage.mello.model.Member;
import com.yourpackage.mello.repository.CardRepository;
import com.yourpackage.mello.repository.MemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final MemberRepository memberRepo;
    private final CardRepository cardRepo;

    public MemberController(MemberRepository memberRepo, CardRepository cardRepo) {
        this.memberRepo = memberRepo;
        this.cardRepo = cardRepo;
    }

    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<Member>> getMembers(@PathVariable Long cardId) {
        return ResponseEntity.ok(memberRepo.findByCardId(cardId));
    }

    @PostMapping
    public ResponseEntity<Member> assignMember(@RequestBody Member member) {
        Card card = cardRepo.findById(member.getCard().getId()).orElse(null);
        if (card == null) return ResponseEntity.notFound().build();

        member.setCard(card);
        return ResponseEntity.ok(memberRepo.save(member));
    }
}