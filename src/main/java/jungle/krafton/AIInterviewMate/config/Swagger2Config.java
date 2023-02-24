package jungle.krafton.AIInterviewMate.config;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class Swagger2Config {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("InterviewMate")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            Parameter parameter = new Parameter()
                    .in(ParameterIn.COOKIE.toString())
                    .schema(new StringSchema()._default("refresh=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDYiLCJpc3MiOiJJTSIsImlhdCI6MTY3NzIxNzYzNywiZXhwIjoxNjc3MzA0MDM3fQ.u_uFIdw7amHS8F4D428uBvLl8mev-vLmZSPz6wKkaV1m0FPZIFB-K-nNXJOJ34jW7eC2jQWUhheuOQEWT-UeYA")
                            .name("refresh"))
                    .name("refresh")
                    .description("REFRESH TOKEN")
                    .required(true);
            operation.addParametersItem(parameter);
            return operation;
        };
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        String jwtSchemeName = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));


        return new OpenAPI()
                .info(new Info().title("InterviewMate API")
                        .description("InterviewMate API 명세서입니다.")
                        .version("v0.0.1"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}

