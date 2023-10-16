package com.example.diaryboard.dto.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter // 쿼리스트링 값을 변수에 바인딩하기 위해 @Setter 사용
public class GetPostPageRequestParam {

    private int page = 0;
    private int size = 8;
    private SortType sortBy = SortType.ID;
    private DirectionType direction = DirectionType.DESC;
    private SearchType searchBy = SearchType.ALL;
    private String keyword = "";
    private LocalDate startDate = LocalDate.of(2000, 1, 1);
    private LocalDate endDate = LocalDate.of(2099, 1, 1);
}
