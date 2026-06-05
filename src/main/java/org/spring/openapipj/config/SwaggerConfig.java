package org.spring.openapipj.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // 웹브라우저에서 Swagger UI를 확인할수있는주소입니다.
    // http://localhost:8090/swagger-ui/index.html

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())   // API에 필요한 보안(Security), 스키마등의 컴포넌트를 설정하는 부분
                .info(info());  		// 문서의 기본정보를 주입
    }

    private Info info() {
        return new Info()
                .title("API Swagger Test") 				// Swagger UI화면 맨위에 노출될 제목
                .description("RestApiController Test Swagger!!")	// 문서에대한 설명
                .version("1.0.0"); 					// API의 현재버전
    }

}
