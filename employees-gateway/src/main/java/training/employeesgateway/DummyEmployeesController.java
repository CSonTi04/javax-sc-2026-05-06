package training.employeesgateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class DummyEmployeesController {

    @GetMapping("/api/dummy-employees")
    public Flux<EmployeeDto> dummyEmployees() {
        return Flux.just(
                new EmployeeDto(1L, "Dummy Alice"),
                new EmployeeDto(2L, "Dummy Bob")
        );
    }
}