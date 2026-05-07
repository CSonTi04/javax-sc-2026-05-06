package training.employeesschemaregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.schema.registry.EnableSchemaRegistryServer;

@SpringBootApplication
@EnableSchemaRegistryServer
public class EmployeesSchemaRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeesSchemaRegistryApplication.class, args);
    }

}
