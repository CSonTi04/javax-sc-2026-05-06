package employees;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
public class MessagingConfiguration {

    @Bean
    public Function<CreateEmployeeRequest, CreateEmployeeResponse> createEmployeeFunction(EmployeesService employeesService) {
        return request -> {
            var employeeEntity = employeesService.createEmployee(new EmployeeResource(request.name()));
            return new CreateEmployeeResponse(employeeEntity.getId(), employeeEntity.getName());
        };
    }
}
