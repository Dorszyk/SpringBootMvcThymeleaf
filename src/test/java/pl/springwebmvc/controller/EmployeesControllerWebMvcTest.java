package pl.springwebmvc.controller;


import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import pl.springwebmvc.infrastructure.database.repository.EmployeeRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmployeesController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Import(GlobalExceptionHandler.class)

public class EmployeesControllerWebMvcTest {

    public MockMvc mockMvc;

    @MockBean
    public EmployeeRepository employeeRepository;

    @Test
    public void thatEmployeesPageLoadsWithModel() throws Exception {
        // given
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        // when, then
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees"))
                .andExpect(model().attributeExists("EmployeeDTO"))
                .andExpect(model().attribute("employees", Matchers.empty()));
    }

    @Test
    public void thatUpdateEmployeeWorksCorrectly_whenEntityExists() throws Exception {
        // given
        Integer id = 1;
        var existing = mock(pl.springwebmvc.infrastructure.database.entity.EmployeeEntity.class);
        when(employeeRepository.findById(id)).thenReturn(Optional.of(existing));

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> dto = EmployeeDTO.builder()
                .employeeId(id)
                .name("John")
                .surname("Doe")
                .salary(new BigDecimal("1234.56"))
                .phone("+48 504 203 260")
                .email("john.doe@example.com")
                .build()
                .asMap();
        dto.forEach(params::add);

        // when, then
        mockMvc.perform(put("/employees/update").params(params))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employees"));
        verify(employeeRepository).findById(id);
        verify(employeeRepository).save(existing);
    }

    @Test
    public void thatUpdateEmployeeReturnsError_whenEntityNotFound() throws Exception {
        // given
        Integer id = 999;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> dto = EmployeeDTO.builder()
                .employeeId(id)
                .name("Missing")
                .surname("Employee")
                .salary(new BigDecimal("10"))
                .phone("+48 504 203 260")
                .email("missing@example.com")
                .build()
                .asMap();
        dto.forEach(params::add);

        // when, then
        mockMvc.perform(put("/employees/update").params(params))
                .andExpect(status().isNotFound())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", Matchers.containsString(String.valueOf(id))))
                .andExpect(view().name("error"));
    }

    @ParameterizedTest
    @MethodSource
    public void thatPhoneValidationWorksCorrectly(Boolean correctPhone, String phone) throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = EmployeeDTO.builder().phone(phone).build().asMap();
        parametersMap.forEach(parameters::add);

        //when, then
        if (correctPhone) {
            mockMvc.perform(post("/employees/add").params(parameters))
                    .andExpect(status().isFound())
                    .andExpect(model().attributeDoesNotExist("errorMessage"))
                    .andExpect(view().name("redirect:/employees"));
        } else {
            mockMvc.perform(post("/employees/add").params(parameters))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeExists("errorMessage"))
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(phone)))
                    .andExpect(view().name("error"));
        }
    }

    public static Stream<Arguments> thatPhoneValidationWorksCorrectly() {
        return Stream.of(
                Arguments.of(false, ""),
                Arguments.of(false, "+48 504 203 260@@"),
                Arguments.of(false, "+48.504.203.260"),
                Arguments.of(false, "+55(123) 456-78-90-"),
                Arguments.of(false, "+55(123) - 456-78-90"),
                Arguments.of(false, "504.203.260"),
                Arguments.of(false, " "),
                Arguments.of(false, "-"),
                Arguments.of(false, "()"),
                Arguments.of(false, "() + ()"),
                Arguments.of(false, "(21 7777"),
                Arguments.of(false, "+48 (21)"),
                Arguments.of(false, "+"),
                Arguments.of(false, " 1"),
                Arguments.of(false, "1"),
                Arguments.of(false, "555-5555-555"),
                Arguments.of(false, "+48 (12) 504 203 260"),
                Arguments.of(false, "+48 (12) 504-203-260"),
                Arguments.of(false, "+48(12)504203260"),
                Arguments.of(false, "+4812504203260"),
                Arguments.of(false, "481250420326ts"),
                Arguments.of(true, "+48 504 203 260")
        );
    }
}
