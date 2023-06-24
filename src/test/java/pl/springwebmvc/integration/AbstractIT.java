package pl.springwebmvc.integration;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.springwebmvc.DemoApplication;

@ActiveProfiles("test")
@Import(PersistenceContainerTestConfiguration.class)
@SpringBootTest(
        classes = {DemoApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractIT {
}
