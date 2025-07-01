package com.openclassrooms.mddapi.dto.topic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TopicDTO {

    private final Long id;

    private final String title;

    private final String description;

}
