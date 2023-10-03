package com.example.diaryboard.dto.post.converter;

import com.example.diaryboard.dto.post.DirectionType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDirectionTypeConverter implements Converter<String, DirectionType> {

    @Override
    public DirectionType convert(String source) {
        return DirectionType.valueOf(source.toUpperCase());
    }
}
