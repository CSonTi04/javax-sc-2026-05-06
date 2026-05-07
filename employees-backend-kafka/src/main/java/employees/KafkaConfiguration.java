package employees;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class KafkaConfiguration {

    @Bean
    public NewTopic eventTopic() {
        return new NewTopic("employees-backend-events", 10, (short) 1);
    }

    @Bean
    public NewTopic requestTopic() {
        return new NewTopic("employees-backend-request", 10, (short) 1);
    }

    @Bean
    public NewTopic responseTopic() {
        return new NewTopic("employees-backend-response", 10, (short) 1);
    }
}
