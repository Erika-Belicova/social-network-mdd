package com.openclassrooms.mddapi.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that provides a ModelMapper bean
 * with customized settings for object mapping.
 * The ModelMapper is configured to skip null values during mapping
 * to prevent overwriting existing values with nulls.
 */
@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        return mapper;
    }

}