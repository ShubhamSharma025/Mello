package com.yourpackage.mello.dto;

import java.util.List;

import org.hibernate.annotations.SecondaryRow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private Long id;
    private String name;
    private List<CardListDto> cardLists;

  
}
