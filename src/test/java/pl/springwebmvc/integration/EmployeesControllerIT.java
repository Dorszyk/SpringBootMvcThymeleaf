package pl.springwebmvc.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.springwebmvc.infrastructure.database.entity.EmployeeEntity;
import pl.springwebmvc.infrastructure.database.repository.EmployeeRepository;

import java.math.BigDecimal;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeesControllerIT extends AbstractIT{

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate;
    private final EmployeeRepository employeeRepository;

    @Test
    void applicationWorksCorrectly(){
        String url = "http://localhost:%s/springwebmvc/employees".formatted(port);

        String page = this.testRestTemplate.getForObject(url, String.class);
        Assertions.assertThat(page).contains("<title>Employees example</title>");
    }

    @Test
    void addEmployee_thenEmployeeIsVisibleOnList() {
        // given
        String addUrl = "http://localhost:%s/springwebmvc/employees/add".formatted(port);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("name", "Anna");
        form.add("surname", "Kowalska");
        form.add("salary", "2500.00");
        form.add("phone", "+48 504 203 260");
        form.add("email", "anna.kowalska@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        // when
        ResponseEntity<String> postResponse = this.testRestTemplate.postForEntity(addUrl, request, String.class);

        // then (redirect expected)
        // Assertions.assertThat(postResponse.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(postResponse.getStatusCode().value()).isEqualTo(302);
        Assertions.assertThat(postResponse.getStatusCode().isSameCodeAs(org.springframework.http.HttpStatus.FOUND)).isTrue();
        Assertions.assertThat(postResponse.getHeaders().getLocation()).isNotNull();
        Assertions.assertThat(postResponse.getHeaders().getLocation().toString()).contains("/springwebmvc/employees");

        // and: new employee visible on list
        String listUrl = "http://localhost:%s/springwebmvc/employees".formatted(port);
        String page = this.testRestTemplate.getForObject(listUrl, String.class);
        Assertions.assertThat(page).contains("Anna").contains("Kowalska");
    }

    @Test
    void showEmployeeDetails_displaysEmployeeInfo() {
        // given
        EmployeeEntity saved = employeeRepository.save(
                EmployeeEntity.builder()
                        .name("Jan")
                        .surname("Nowak")
                        .email("jan.nowak@example.com")
                        .phone("+48 600 100 200")
                        .salary(new BigDecimal("4200.50"))
                        .build()
        );

        String url = "http://localhost:%s/springwebmvc/employees/show/%s".formatted(port, saved.getEmployeeId());

        // when
        String page = this.testRestTemplate.getForObject(url, String.class);

        // then
        Assertions.assertThat(page).contains("Jan");
        Assertions.assertThat(page).contains("Nowak");
        Assertions.assertThat(page).contains("jan.nowak@example.com");
        Assertions.assertThat(page).contains("+48 600 100 200");
    }
}
