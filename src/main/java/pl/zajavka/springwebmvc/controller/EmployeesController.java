package pl.zajavka.springwebmvc.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.springwebmvc.infrastructure.database.entity.EmployeeEntity;
import pl.zajavka.springwebmvc.infrastructure.database.repository.EmployeeRepository;

import java.util.List;

@Controller
@RequestMapping("/employees")
@AllArgsConstructor
public class EmployeesController {

    private EmployeeRepository employeeRepository;

    @GetMapping
    public String employees(Model model) {
        List<EmployeeEntity> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        model.addAttribute("EmployeeDTO", new EmployeeDTO());
        return "employees";
    }

    @GetMapping("/show/{employeeId}")
    public String showEmployeeDetails(@PathVariable Integer employeeId, Model model) {
        EmployeeEntity employeeDetails = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("EmployeeEntity not found, employeeId: [%s]", employeeId)));

        model.addAttribute("employee", employeeDetails);

        return "employeeDetails";
    }


    @PostMapping("/add")
    public String addEmployee(
            @Valid @ModelAttribute("EmployeeDTO") EmployeeDTO employeeDTO
    ) {
        EmployeeEntity newEmployee = EmployeeEntity.builder()
                .name(employeeDTO.getName())
                .surname(employeeDTO.getSurname())
                .salary(employeeDTO.getSalary())
                .phone(employeeDTO.getPhone())
                .email(employeeDTO.getEmail())
                .build();
        employeeRepository.save(newEmployee);

        return "redirect:/employees";
    }

    @DeleteMapping("/delete/{employeeId}")
    public String deleteEmployee(@PathVariable Integer employeeId) {
        employeeRepository.deleteById(employeeId);

        return "redirect:/employees";
    }


    @PutMapping("/update")
    public String updateEmployee(
            @Valid @ModelAttribute("EmployeeDTO") EmployeeDTO employeeDTO
    ) {
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeDTO.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "EmployeeEntity not found, employeeId: [%s]".formatted(employeeDTO.getEmployeeId())));

        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setSurname(employeeDTO.getSurname());
        employeeEntity.setSalary(employeeDTO.getSalary());
        employeeEntity.setPhone(employeeDTO.getPhone());
        employeeEntity.setEmail(employeeDTO.getEmail());
        employeeRepository.save(employeeEntity);

        return "redirect:/employees";
    }

}
