package com.yourpackage.mello.repository;

//package com.yourname.mello.repository;

import com.yourpackage.mello.model.Board;
import com.yourpackage.mello.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
  List<Board>findByUser(User user);
    
}