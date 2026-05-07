package employees;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
@Configuration(proxyBeanMethods = false)
@Slf4j
public class MessagingConfig {

    @Bean
    public Consumer<CreateEmployeeResponse> employeeCreated() {
        return request -> log.info("Employee created: id={}, name={}", request.id(), request.name());
    }
}
