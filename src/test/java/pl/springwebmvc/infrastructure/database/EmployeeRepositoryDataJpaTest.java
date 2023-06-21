package pl.springwebmvc.infrastructure.database;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import pl.springwebmvc.infrastructure.database.entity.EmployeeEntity;
import pl.springwebmvc.infrastructure.database.repository.EmployeeRepository;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static pl.springwebmvc.util.EntityFixtures.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yaml")
@AllArgsConstructor (onConstructor = @__(@Autowired))
public class EmployeeRepositoryDataJpaTest extends AbstractJpaIT{

    public EmployeeRepository employeeRepository;

    @Test
    void thatEmployeeCanBeSavedCorrectly(){
        // given
        var employees = List.of(someEmployee1(), someEmployee2(), someEmployee3());
        employeeRepository.saveAllAndFlush(employees);

        // when
        List<EmployeeEntity> employeeEntitiesFound = employeeRepository.findAll();

        // then
        assertThat(employeeEntitiesFound.size()).isEqualTo(3);
    }

}
