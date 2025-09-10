package com.yourpackage.mello.service;


import org.springframework.stereotype.Service;

import com.yourpackage.mello.dto.CardListDto;
import com.yourpackage.mello.dto.CreateListRequest;
import com.yourpackage.mello.mapper.CardListMapper;
import com.yourpackage.mello.model.Board;
import com.yourpackage.mello.model.CardList;
import com.yourpackage.mello.repository.BoardRepository;
import com.yourpackage.mello.repository.CardListRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CardListService {

    private final CardListRepository cardListRepository;
    private final CardListMapper cardListMapper;
    private final BoardRepository boardRepository ;


        @Transactional
     public CardListDto createList(CreateListRequest request){
        Board board =boardRepository.findById(request.getBoardId())
          .orElseThrow(()-> new RuntimeException("Board not found"));

          CardList list =new CardList();
          list.setName(request.getName());
          list.setBoard(board);
        
          CardList saveCardList=cardListRepository.save(list);
         return cardListMapper.toDto(saveCardList);
     }
    
     public void deleteList(Long id){
        if(!cardListRepository.existsById(id)){
            throw new RuntimeException("not found");
        }
        cardListRepository.deleteById(id);
     }
    
    
    }