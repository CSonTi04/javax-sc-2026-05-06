package employees;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Proxy beans methods is set to false because we don't need to call any method of this class from another method of the same class, so we can avoid the overhead of creating a proxy for this class.
//Gyorsabb 👀
//Nem proxy bean-eket használunk, mert nincs szükségünk arra, hogy egy metódus hívjon egy másik metódust ugyanabban az osztályban, így elkerülhetjük a proxy létrehozásának overhead-jét.
@Configuration(proxyBeanMethods = false)
public class KafkaConfiguration {

    @Bean
    public NewTopic helloTopic() {
        return new NewTopic("hello", 10, (short) 1);
    }
}
