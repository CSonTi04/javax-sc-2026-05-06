package employees;

import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaGateway {
    //Lehetne saját gateway annotáció is, de ez is megteszi
    private final EmployeesService employeesService;
    private final KafkaTemplate<String, EmployeeHasBeenCreatedEvent> kafkaTemplate;

    @KafkaListener(topics = "employees-backend-request", groupId = "employees-backend")
    public void handleRequest(CreateEmployeeRequest request) {
        log.info("Received request {}", request);
        employeesService.createEmployee(new EmployeeResource(request.name()));
    }

    @EventListener
    public void handleEvent(EmployeeHasBeenCreatedEvent event) {
        log.info("Received event {}", event);
        kafkaTemplate.send("employees-backend-events", event);
    }
}
