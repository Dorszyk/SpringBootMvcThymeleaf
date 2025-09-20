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

    @Test
    void thatEmployeeCanBeFoundAndUpdatedByEmail() {
        // given
        EmployeeEntity employee = someEmployee1();
        employeeRepository.saveAndFlush(employee);

        // when
        EmployeeEntity found = employeeRepository.findByEmail(employee.getEmail())
                .orElseThrow(() -> new IllegalStateException("Employee not found by email"));

        // then
        assertThat(found.getName()).isEqualTo(employee.getName());
        assertThat(found.getSurname()).isEqualTo(employee.getSurname());

        // when (update)
        found.setSalary(found.getSalary().add(new java.math.BigDecimal("1000.00")));
        found.setPhone("600 700 800");
        employeeRepository.saveAndFlush(found);

        // then (verify persisted changes)
        EmployeeEntity afterUpdate = employeeRepository.findById(found.getEmployeeId())
                .orElseThrow(() -> new IllegalStateException("Employee not found by id after update"));
        assertThat(afterUpdate.getSalary()).isEqualTo(found.getSalary());
        assertThat(afterUpdate.getPhone()).isEqualTo("600 700 800");
        assertThat(afterUpdate.getEmail()).isEqualTo(employee.getEmail()); // email niezmieniony
    }
}
