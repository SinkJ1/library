package sinkj1.library.config;

import static springfox.documentation.builders.PathSelectors.regex;

import com.google.common.collect.Lists;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TENANT_ID = "X-TENANT-ID";
    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    @Bean
    @Primary
    public Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger");
        springfox.documentation.service.Contact contact = new Contact("", "", "");

        List<VendorExtension> vext = new ArrayList<>();
        ApiInfo apiInfo = new ApiInfo(
            "Backend API",
            "This is the best stuff since sliced bread - API",
            "6.6.6",
            "https://justrocket.de",
            contact,
            "MIT",
            "https://justrocket.de",
            vext
        );

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo)
            .pathMapping("/")
            .groupName("main")
            .apiInfo(ApiInfo.DEFAULT)
            .forCodeGeneration(true)
            .genericModelSubstitutes(ResponseEntity.class)
            .ignoredParameterTypes(Pageable.class)
            .ignoredParameterTypes(java.sql.Date.class)
            .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
            .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
            .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
            .securityContexts(Lists.newArrayList(securityContext()))
            .securitySchemes(Lists.newArrayList(apiKey()))
            .useDefaultResponseMessages(false);

        docket = docket.select().paths(regex(DEFAULT_INCLUDE_PATTERN)).build();
        return docket;
    }

    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider) {
        return () -> {
            SwaggerResource wsResource = new SwaggerResource();
            wsResource.setName("Documentation");
            wsResource.setSwaggerVersion("2.0");
            wsResource.setLocation("/doc/swagger.yml");
            List<SwaggerResource> resources = new ArrayList<>();
            resources.add(wsResource);
            return resources;
        };
    }

    private List<ApiKey> apiKey() {
        return List.of(new ApiKey("Authorization", AUTHORIZATION_HEADER, "header"));
    }

    private springfox.documentation.spi.service.contexts.SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN)).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("Authorization", authorizationScopes));
    }
}
