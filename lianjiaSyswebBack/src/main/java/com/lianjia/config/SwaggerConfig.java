package com.lianjia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public Docket createRestApi() {
        List<RequestParameter> pars = new ArrayList<>();
        RequestParameter tokenPar = new RequestParameterBuilder()
                .name("Authorization")
                .description("JWT Token")
                .in(ParameterType.HEADER)
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .required(false)
                .build();
        pars.add(tokenPar);
        
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.lianjia.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(pars);
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("恋家环保商户端管理后台 API")
                .description("恋家环保商户端管理后台接口文档")
                .version("1.0.0")
                .build();
    }
}
