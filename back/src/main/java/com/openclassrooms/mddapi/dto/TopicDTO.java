package com.openclassrooms.mddapi.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TopicDTO {

    final Long id;

    final private String title;

    final private String description;

}
