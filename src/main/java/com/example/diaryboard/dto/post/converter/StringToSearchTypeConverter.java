package com.example.diaryboard.dto.post.converter;

import com.example.diaryboard.dto.post.SearchType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSearchTypeConverter implements Converter<String, SearchType> {

    @Override
    public SearchType convert(String source) {
        return SearchType.valueOf(source.toUpperCase());
    }
}
