package employees;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
//lehetne rekord!
//https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/
public class EmployeeResource {

    private Long id;

    @NotBlank
    private String name;

    public EmployeeResource(String name) {
        this.name = name;
    }
}
