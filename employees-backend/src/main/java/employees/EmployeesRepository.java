package employees;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeesRepository extends JpaRepository<Employee, Long> {

    //projection query -> egyből dto-t adhat vissza
    @Query("select new employees.EmployeeResource(e.id, e.name) from Employee e")
    List<EmployeeResource> findAllResources();
}
