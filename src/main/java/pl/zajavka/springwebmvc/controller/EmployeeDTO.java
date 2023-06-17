package pl.zajavka.springwebmvc.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Integer employeeId;
    private String name;
    private String surname;
    private BigDecimal salary;
    @Size(min= 7, max = 15)
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String phone;
    @Email
    private String email;

    public Map<String, String> asMap(){
        Map<String, String> result = new HashMap<>();
        Optional.ofNullable(employeeId).ifPresent(value -> result.put("employeeId",value.toString()));
        Optional.ofNullable(name).ifPresent(value -> result.put("name",value.toString()));
        Optional.ofNullable(surname).ifPresent(value -> result.put("surname",value.toString()));
        Optional.ofNullable(salary).ifPresent(value -> result.put("salary",value.toString()));
        Optional.ofNullable(phone).ifPresent(value -> result.put("phone",value.toString()));
        Optional.ofNullable(email).ifPresent(value -> result.put("email",value.toString()));
        return result;
    }
}
