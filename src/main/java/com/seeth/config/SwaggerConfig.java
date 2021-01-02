package com.seeth.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
    	
    	RequestParameterBuilder parameterBuilder = new RequestParameterBuilder()
    		    .in(ParameterType.HEADER)
    		    .name("Authorization")
    		    .required(true)
    		    .query(param -> param.model(model -> model.scalarModel(ScalarType.STRING)));
    	
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .globalRequestParameters(Collections.singletonList(parameterBuilder.build()));
    }

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Consultancy Management Portal Services")
                .description("Cyber Edge")
                .version("1.0.0")
                .contact(new Contact("Nidheesh", "", "xyz@cyberedge.com"))
                .build();
    }

}
