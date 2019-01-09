package com.daniel.licenta.calendargenerator;

import com.daniel.licenta.calendargenerator.algorithm.inputmodel.ConfigData;
import com.daniel.licenta.calendargenerator.business.model.*;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebMvc
@EnableJpaAuditing
public class CalendarGeneratorApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(CalendarGeneratorApplication.class, args);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*****");
    }

    @Bean
    public DozerBeanMapper mapper() throws Exception {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(objectMappingBuilder);
        return mapper;
    }

    BeanMappingBuilder objectMappingBuilder = new BeanMappingBuilder() {
        @Override
        protected void configure() {
            mapping(Course.class, Course.class, TypeMappingOptions.mapNull(false));
            mapping(Lecture.class, Lecture.class, TypeMappingOptions.mapNull(false));
            mapping(Room.class, Room.class, TypeMappingOptions.mapNull(false));
            mapping(StudentClass.class, StudentClass.class, TypeMappingOptions.mapNull(false));
            mapping(Teacher.class, Teacher.class, TypeMappingOptions.mapNull(false));
            mapping(ConfigData.class, ConfigData.class, TypeMappingOptions.mapNull(false));
        }
    };
}

