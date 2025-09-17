package com.yourpackage.mello.service;

import com.yourpackage.mello.dto.BoardDto;
import com.yourpackage.mello.mapper.BoardMapper;
import com.yourpackage.mello.model.Board;
import com.yourpackage.mello.model.CardList;
import com.yourpackage.mello.model.User;
import com.yourpackage.mello.repository.BoardRepository;
import com.yourpackage.mello.repository.CardListRepository;
import com.yourpackage.mello.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final UserRepository userRepository;
    private final CardListRepository cardListRepository;

      // ✅ Get all boards for a specific user
  public List<BoardDto> getAllBoards(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return boardRepository.findByUser(user).stream()
                .map(boardMapper::toDto)
                .toList();
    }

    public BoardDto getBoardById(Long id,String username) {

        User user =userRepository.findByUsername(username)
        .orElseThrow(()-> new RuntimeException("usernot found"));
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

           if(!board.getUser().getId().equals(user.getId())){
            throw new RuntimeException("unauthorized access to this board");
           }     
          List<CardList> cardLists=cardListRepository.findByBoardIdWithCards(id);
          board.setCardLists(cardLists);      
        return boardMapper.toDto(board);
    }


    public BoardDto createBoard(BoardDto dto,String username) {

         User user=userRepository.findByUsername(username)
         .orElseThrow(()-> new RuntimeException("user not found"));
        Board board=boardMapper.toEntity(dto);
        board.setUser(user);
        Board savedBoard = boardRepository.save(board);
        return boardMapper.toDto(savedBoard);
    }

    public BoardDto updateBoard(Long id,BoardDto dto){
        Board board=boardRepository.findById(id)
         .orElseThrow(()->new RuntimeException("Board not found"+id));
         board.setName(dto.getName());
        
         Board updateBoard=boardRepository.save(board);
         return boardMapper.toDto(updateBoard);
    }

      public void deleteBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        boardRepository.delete(board);
    }
}
