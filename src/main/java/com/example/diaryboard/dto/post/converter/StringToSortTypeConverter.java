package com.example.diaryboard.dto.post.converter;

import com.example.diaryboard.dto.post.SortType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSortTypeConverter implements Converter<String, SortType> {

    @Override
    public SortType convert(String source) {
        return SortType.valueOf(source.toUpperCase());
    }
}
