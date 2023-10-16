package com.example.diaryboard.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GetPostPageRequestParam {

    private int page;
    private int size;
    private SortType sortBy;
    private DirectionType direction;
    private SearchType searchBy;
    private String keyword;
    private LocalDate startDate;
    private LocalDate endDate;
}
