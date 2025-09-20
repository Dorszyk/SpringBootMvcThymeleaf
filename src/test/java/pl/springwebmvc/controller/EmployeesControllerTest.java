package pl.springwebmvc.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ExtendedModelMap;
import pl.springwebmvc.infrastructure.database.entity.EmployeeEntity;
import pl.springwebmvc.infrastructure.database.repository.EmployeeRepository;
import pl.springwebmvc.util.EntityFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
public class EmployeesControllerTest {
    @Mock
    public EmployeeRepository employeeRepository;

    @InjectMocks
    public EmployeesController employeesController;

    @Test
    public void thatRetrievingEmployeeDetailsWorksCorrectly() throws Exception {
        //given
        int employeeId = 10;
        EmployeeEntity employeeEntity = EntityFixtures.someEmployee1();
        ExtendedModelMap model = new ExtendedModelMap();

        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employeeEntity));
        //when
        String result = employeesController.showEmployeeDetails(employeeId, model);

        //then
        assertThat(result).isEqualTo("employeeDetails");
        assertThat(model.getAttribute("employee")).isEqualTo(employeeEntity);
    }
    @Test
    public void thatRetrievingEmployeeDetailsThrowsWhenEmployeeNotFound() {
        // given
        int employeeId = 999;
        ExtendedModelMap model = new ExtendedModelMap();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> employeesController.showEmployeeDetails(employeeId, model))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.valueOf(employeeId));
    }
    @Test
    public void thatDeletingEmployeeWorksCorrectly() {
        // given
        int employeeId = 20;

        // when
        String result = employeesController.deleteEmployee(employeeId);

        // then
        assertThat(result).isEqualTo("redirect:/employees");
        Mockito.verify(employeeRepository).deleteById(employeeId);
    }

}
