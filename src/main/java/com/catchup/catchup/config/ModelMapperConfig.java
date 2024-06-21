package com.catchup.catchup.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    // ModelMapper 라이브러리 추가해야됨

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
