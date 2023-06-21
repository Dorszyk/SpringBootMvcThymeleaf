package pl.springwebmvc.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeesControllerIT extends AbstractIT{

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate;

    @Test
    void applicationWorksCorrectly(){
        String url = "http://localhost:%s/springwebmvc/employees".formatted(port);

        String page = this.testRestTemplate.getForObject(url, String.class);
        Assertions.assertThat(page).contains("<title>Employees example</title>");
    }
}
