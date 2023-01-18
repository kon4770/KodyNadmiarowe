package com.RedundancyCodes.pw.edu.pl;

import com.RedundancyCodes.pw.edu.pl.services.impl.RedundancyCode;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedundancyCodesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedundancyCodesApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }

    @Bean
    public RedundancyCode redundancyCode() {
        RedundancyCode redundancyCode = new RedundancyCode();
        return redundancyCode;
    }
}