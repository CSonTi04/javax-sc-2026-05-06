package employees;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@EnableConfigurationProperties(EmployeesProperties.class)
//meg lehet adni, hogy mit használjon a RestClientHttpServiceGroupConfigurer-ben
@ImportHttpServices(group = "employees-backend", types = EmployeesClient.class)
@Slf4j
public class ClientConfig {

    @Bean
    RestClientHttpServiceGroupConfigurer groupConfigurer(EmployeesProperties employeesProperties, OAuth2AuthorizedClientManager authorizedClientManager) {
        log.info("Employees backend url: {}", employeesProperties.getBackendUrl());
        var interceptor = new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);

        return groups ->
            groups.filterByName("employees-backend").forEachClient((group, builder) -> {
                log.debug("Configuring RestClient group: {}", group);
                builder
                    .baseUrl(employeesProperties.getBackendUrl())
                    .requestInterceptor(interceptor);
            });
    }
//    Ez a spring boot 3-mas, ezt váltotta ki a @ImportHttpServices, de így is működik
//    @Bean
//    public EmployeesClient employeesClient(RestClient.Builder builder, EmployeesProperties employeesProperties) {
//        var restClient = builder
//                .baseUrl(employeesProperties.getBackendUrl())
//                .build();
//        var adapter = RestClientAdapter.create(restClient);
//        var factory = HttpServiceProxyFactory
//                .builderFor(adapter).build();
//        return factory.createClient(EmployeesClient.class);
//    }

}
