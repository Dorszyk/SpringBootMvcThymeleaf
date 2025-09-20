package pl.springwebmvc.integration;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.springwebmvc.DemoApplication;
import pl.springwebmvc.security.TestSecurityConfiguration;

@ActiveProfiles("test")
@Import({PersistenceContainerTestConfiguration.class, TestSecurityConfiguration.class})
@SpringBootTest(
        classes = {DemoApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractIT {
}
